package org.batfish.z3;

import org.batfish.z3.node.*;

public class ReachableQuerySynthesizer implements QuerySynthesizer {

   private String _queryText;

   public ReachableQuerySynthesizer(String originationNode,
                                    String acceptNode,
                                    BooleanExpr filter) {
      OriginateExpr originate = new OriginateExpr(originationNode);
      RuleExpr injectSymbolicPackets = new RuleExpr(originate);
      AndExpr queryConditions = new AndExpr();
      if (acceptNode != null) {
         NodeAcceptExpr nodeAccept = new NodeAcceptExpr(acceptNode);
         queryConditions.addConjunct(nodeAccept);
      }
      else {
         queryConditions.addConjunct(AcceptExpr.INSTANCE);
      }
      queryConditions.addConjunct(SaneExpr.INSTANCE);
      if (filter != null) {
         queryConditions.addConjunct(filter);
      }
      RuleExpr queryRule = new RuleExpr(queryConditions,
            QueryRelationExpr.INSTANCE);
      QueryExpr query = new QueryExpr(QueryRelationExpr.INSTANCE);
      StringBuilder sb = new StringBuilder();
      injectSymbolicPackets.print(sb, 0);
      sb.append("\n");
      queryRule.print(sb, 0);
      sb.append("\n");
      query.print(sb, 0);
      sb.append("\n");
      String queryText = sb.toString();
      _queryText = queryText;

   }

   public ReachableQuerySynthesizer(String originatorNode, BooleanExpr filterText) {
      this(originatorNode, null, filterText);
   }

   public ReachableQuerySynthesizer(String originatorNode, String acceptNode) {
      this(originatorNode, acceptNode, null);
   }

   @Override
   public String getQueryText() {
      return _queryText;
   }

}
