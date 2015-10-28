#!/usr/bin/env bash

batfish_confirm_analyze_reachability() {
   BATFISH_CONFIRM=batfish_confirm batfish_analyze_reachability $@
}
export -f batfish_confirm_analyze_reachability

batfish_analyze_reachability() {
   local TEST_RIG_RELATIVE=$1
   shift
   local PREFIX=$1
   shift
   local MACHINES="$@"
   local NUM_MACHINES="$#"
   if [ -z "$PREFIX" ]; then
         echo "ERROR: Empty prefix" 1>&2
         return 1
   fi
   if [ -z "$BATFISH_CONFIRM" ]; then
      local BATFISH_CONFIRM=true
   fi
   local OLD_PWD=$PWD
   if [ "$(echo $TEST_RIG_RELATIVE | head -c1)" = "/" ]; then
      local TEST_RIG=$TEST_RIG_RELATIVE
   else
      local TEST_RIG=$PWD/$TEST_RIG_RELATIVE
   fi
   local QUERY_PATH=$OLD_PWD/$PREFIX-query
   local REACHABILITY_QUERY_BASE_PATH=$QUERY_PATH/reachability-query

   local BGP=$OLD_PWD/$PREFIX-bgp
   local DP_DIR=$OLD_PWD/$PREFIX-dp
   local DUMP_DIR=$OLD_PWD/$PREFIX-dump
   local FLOWS=$OLD_PWD/$PREFIX-flows
   local INDEP_SERIAL_DIR=$OLD_PWD/$PREFIX-indep
   local NODE_SET_PATH=$OLD_PWD/$PREFIX-node-set
   local OSPF=$OLD_PWD/$PREFIX-ospf
   local POLICY=$OLD_PWD/$PREFIX-policy
   local REACH_PATH=$OLD_PWD/$PREFIX-reach.smt2
   local ROUTES=$OLD_PWD/$PREFIX-routes
   local VENDOR_SERIAL_DIR=$OLD_PWD/$PREFIX-vendor
   local WORKSPACE=batfish-$USER-$PREFIX

   echo "Parse vendor configuration files and serialize vendor structures"
   $BATFISH_CONFIRM && { batfish_serialize_vendor $TEST_RIG $VENDOR_SERIAL_DIR || return 1 ; }

   echo "Parse vendor structures and serialize vendor-independent structures"
   $BATFISH_CONFIRM && { batfish_serialize_independent $VENDOR_SERIAL_DIR $INDEP_SERIAL_DIR || return 1 ; }

   echo "Compute the fixed point of the control plane"
   $BATFISH_CONFIRM && { batfish_compile $WORKSPACE $TEST_RIG $DUMP_DIR $INDEP_SERIAL_DIR || return 1 ; }

   echo "Query bgp"
   $BATFISH_CONFIRM && { batfish_query_bgp $BGP $WORKSPACE || return 1 ; }

   echo "Query ospf"
   $BATFISH_CONFIRM && { batfish_query_ospf $OSPF $WORKSPACE || return 1 ; }

   echo "Query policy"
   $BATFISH_CONFIRM && { batfish_query_policy $POLICY $WORKSPACE || return 1 ; }

   echo "Query routes"
   $BATFISH_CONFIRM && { batfish_query_routes $ROUTES $WORKSPACE || return 1 ; }

   echo "Query data plane predicates"
   $BATFISH_CONFIRM && { batfish_query_data_plane $WORKSPACE $DP_DIR || return 1 ; }

   echo "Extract z3 reachability relations"
   $BATFISH_CONFIRM && { batfish_generate_z3_reachability $DP_DIR $INDEP_SERIAL_DIR $REACH_PATH $NODE_SET_PATH || return 1 ; }

   echo "Find reachability packet constraints"
   $BATFISH_CONFIRM && { batfish_find_reachability_packet_constraints $REACH_PATH $QUERY_PATH $REACHABILITY_QUERY_BASE_PATH $NODE_SET_PATH "$MACHINES" "$NUM_MACHINES" || return 1 ; }

   echo "Generate reachability concretizer queries"
   $BATFISH_CONFIRM && { batfish_generate_reachability_concretizer_queries $REACHABILITY_QUERY_BASE_PATH $NODE_SET_PATH "$MACHINES" "$NUM_MACHINES"|| return 1 ; }

   echo "Inject concrete packets into network model"
   $BATFISH_CONFIRM && { batfish_inject_packets $WORKSPACE $QUERY_PATH $DUMP_DIR || return 1 ; }

   echo "Query flow results from LogicBlox"
   $BATFISH_CONFIRM && { batfish_query_flows $FLOWS $WORKSPACE || return 1 ; }
}
export -f batfish_analyze_reachability

batfish_find_reachability_packet_constraints() {
   batfish_date
   echo ": START: Find reachability packet constraints"
   batfish_expect_args 6 $# || return 1
   local REACH_PATH=$1
   local QUERY_PATH=$2
   local QUERY_BASE_PATH=$3
   local NODE_SET_PATH=$4
   local MACHINES="$5"
   local NUM_MACHINES="$6"
   local NODE_SET_TEXT_PATH=${NODE_SET_PATH}.txt
   local OLD_PWD=$PWD
   local SERVER_OPTS=
   mkdir -p $QUERY_PATH
   cd $QUERY_PATH
   batfish -reachability -reachabilitypath $QUERY_BASE_PATH -nodes $NODE_SET_PATH || return 1
   if [ -n "$NUM_MACHINES" -a "$NUM_MACHINES" -gt 0 ]; then
      for MACHINE in $MACHINES; do
         #set server options for GNU parallel
         local SERVER_OPTS="$SERVER_OPTS -S $MACHINE"
         # copy necessary files to remote machines
         ssh $MACHINE mkdir -p $QUERY_PATH || return 1
         rsync -av -rsh=ssh --stats --progress $REACH_PATH $MACHINE:$REACH_PATH || return 1
         rsync -av -rsh=ssh --stats --progress ${QUERY_BASE_PATH}* $MACHINE:$QUERY_PATH/ || return 1
      done
   fi
   sort -R $NODE_SET_TEXT_PATH | $BATFISH_PARALLEL $SERVER_OPTS batfish_find_reachability_packet_constraints_helper {} $REACH_PATH $QUERY_BASE_PATH
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
   if [ -n "$NUM_MACHINES" -a "$NUM_MACHINES" -gt 0 ]; then
      for MACHINE in $MACHINES; do
         # copy output files from remote machines
         rsync -av -rsh=ssh --stats --progress $MACHINE:$QUERY_PATH/. $QUERY_PATH/. || return 1
      done
   fi
   cd $OLD_PWD
   batfish_date
   echo ": END: Find reachability packet constraints"
}
export -f batfish_find_reachability_packet_constraints

batfish_find_reachability_packet_constraints_helper() {
   batfish_expect_args 3 $# || return 1
   local NODE=$1
   local REACH_PATH=$2
   local QUERY_BASE_PATH=$3
   batfish_date
   local QUERY_PATH=${QUERY_BASE_PATH}-${NODE}.smt2
   local QUERY_OUTPUT_PATH=${QUERY_PATH}.out
   echo ": START: Find reachability packet constraints for \"${NODE}\" (\"${QUERY_OUTPUT_PATH}\")"
   cat $REACH_PATH $QUERY_PATH | batfish_time $BATFISH_Z3_DATALOG -smt2 -in 3>&1 1> $QUERY_OUTPUT_PATH 2>&3
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
   batfish_date
   echo ": END: Find reachability packet constraints for \"$NODE\" (\"${QUERY_OUTPUT_PATH}\")"
}
export -f batfish_find_reachability_packet_constraints_helper

batfish_generate_reachability_concretizer_queries() {
   batfish_date
   echo ": START: Generate reachability concretizer queries"
   batfish_expect_args 4 $# || return 1
   local QUERY_BASE_PATH=$1
   local NODE_SET_PATH=$2
   local MACHINES="$3"
   local NUM_MACHINES="$4"
   local NODE_SET_TEXT_PATH=${NODE_SET_PATH}.txt
   local QUERY_PATH="$(dirname $QUERY_BASE_PATH)"
   local OLD_PWD=$PWD
   cd $QUERY_PATH
   local SERVER_OPTS=
   if [ -n "$NUM_MACHINES" -a "$NUM_MACHINES" -gt 0 ]; then
      for MACHINE in $MACHINES; do
         #set server options for GNU parallel
         local SERVER_OPTS="$SERVER_OPTS -S $MACHINE"
         # copy necessary files to remote machines
         rsync -av -rsh=ssh --stats --progress $QUERY_PATH/. $MACHINE:$QUERY_PATH/. || return 1
      done
   fi
   cat $NODE_SET_TEXT_PATH | $BATFISH_PARALLEL $SERVER_OPTS batfish_generate_reachability_concretizer_queries_helper {} $QUERY_BASE_PATH \;
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
   if [ -n "$NUM_MACHINES" -a "$NUM_MACHINES" -gt 0 ]; then
      for MACHINE in $MACHINES; do
         # copy output files from remote machines
         rsync -av -rsh=ssh --stats --progress $MACHINE:$QUERY_PATH/. $QUERY_PATH/. || return 1
      done
   fi
   cd $OLD_PWD
   batfish_date
   echo ": END: Generate reachability concretizer queries"
}
export -f batfish_generate_reachability_concretizer_queries

batfish_generate_reachability_concretizer_queries_helper() {
   batfish_expect_args 2 $# || return 1
   local NODE=$1
   local QUERY_BASE_PATH=$2
   local QUERY_OUT=${QUERY_BASE_PATH}-${NODE}.smt2.out
   local CONCRETIZER_QUERY_BASE_PATH=${QUERY_BASE_PATH}-${NODE}-concrete
   local QUERY_DIR=$(dirname $QUERY_BASE_PATH)
   cd $QUERY_DIR
   batfish_date
   echo ": START: Generate reachability concretizer queries for node \"${NODE}\""
   batfish -conc -concin $QUERY_OUT -concout $CONCRETIZER_QUERY_BASE_PATH || return 1
   find $PWD -regextype posix-extended -regex "${CONCRETIZER_QUERY_BASE_PATH}-[0-9]+.smt2" | \
      $BATFISH_NESTED_PARALLEL batfish_generate_concretizer_query_output {} $NODE \;
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
   batfish_date
   echo ": END: Generate reachability concretizer queries for node \"${NODE}\""
   echo
}
export -f batfish_generate_reachability_concretizer_queries_helper

