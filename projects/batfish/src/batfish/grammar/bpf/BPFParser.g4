parser grammar BPFParser;

options {
    superClass = 'batfish.grammar.BatfishParser';
    tokenVocab = BPFLexer;
}

@header {
package batfish.grammar.bpf;
}

/* expressions */
prog: expr EOF
    | EOF
    ;

expr: term                  # Singleton
    | expr AND term         # Conjunction
    | expr OR term          # Disjunction
    ;

term: NOT term              # Negation
    | pqual dqual tqual id  # PDTQualId
    | pqual dqual id        # PDQualId
    | pqual tqual id        # PTQualId
    | dqual tqual id        # DTQualId
    | pqual id              # PQualId
    | dqual id              # DQualId
    | tqual id              # TQualId
    | id                    # IdOnly
    | pqual                 # ProtoOnly
    | LPAREN expr RPAREN    # ParenExpr
    ;

/* entities */
id: network_id              # Network
  | port_num                # Port
  | port_range              # PortRange
  | ID                      # PortName
  | NOT network_id          # NotNetwork
  | NOT port_num            # NotPort
  | NOT port_range          # NotPortRange
  | NOT ID                  # NotPortName
  ;

network_id: CIDR            # Prefix
          | HID MASK HID    # Masked
          | HID             # Host
          | NID1            # Net1
          | NID2            # Net2
          | NID3            # Net3
          ;

port_num: NUM                       # PortNum
        | NID1                      # SmallPortNum
        | LPAREN port_num RPAREN    # ParenPortNum
        ;

port_range: RANGE                       # PortNumRange
          | LPAREN port_range RPAREN    # ParenPortNumRange
          ;

/* protocol qualifier */
pqual: IP       # ProtoIP
     | TCP      # ProtoTCP
     | UDP      # ProtoUDP
     | SCTP     # ProtoSCTP
     | ICMP     # ProtoICMP
     | IGMP     # ProtoIGMP
     ;

/* direction qualifier */
dqual: SRC OR DST   # DirSrcOrDst
     | DST OR SRC   # DirDstOrSrc
     | SRC AND DST  # DirSrcAndDst
     | DST AND SRC  # DirDstAndSrc
     | SRC          # DirSrc
     | DST          # DirDst
     ;

/* address type qualifier */
tqual: HOST         # TypeHost
     | NET          # TypeNet
     | PORT         # TypePort
     | PORTRANGE    # TypePortRange
     ;