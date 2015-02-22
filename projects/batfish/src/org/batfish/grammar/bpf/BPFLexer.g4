lexer grammar BPFLexer;

options {
    superClass = 'org.batfish.grammar.BatfishLexer';
}

@header {
package org.batfish.grammar.bpf;
}

WS : ( '\t' | ' ' | '\r' | '\n' )+ -> skip ;
LPAREN: '(' ;
RPAREN: ')' ;

/* proto qualifier */

IP: 'ip' ;
TCP: 'tcp' ;
UDP: 'udp' ;
SCTP: 'sctp' ;
ICMP: 'icmp' ;
IGMP: 'igmp' ;
// IPV6: 'ip6' ;
// ICMPV6: 'icmp6' ;

/* type qualifier */

HOST: 'host' ;
NET: 'net' ;
MASK: 'mask' ;
PORT: 'port' ;
PORTRANGE: 'portrange' ;

/* dir qualifier */

DST: 'dst' ;
SRC: 'src' ;

/* logical */

AND: 'and' | '&&' ;
OR: 'or' | '||' ;
NOT: 'not' | '!' ;

/* value */

CIDR: F_IPv4Address '/' F_IPv4Prefix ;
HID: F_IPv4Address ;

NID3: F_IPv4Part '.' F_IPv4Part '.' F_IPv4Part ;
NID2: F_IPv4Part '.' F_IPv4Part ;
NID1: F_IPv4Part ;

RANGE: F_Digit+ '-' F_Digit+
     | ('0' [xX] F_HexDigit+) '-' ('0' [xX] F_HexDigit+)
     ;

NUM: F_Digit+ | ('0' [xX] F_HexDigit+) ;

ID: F_Letter ( F_AlphaNum | [-_.] )* ;


/* fragments */

fragment
F_PositiveDigit: '1'..'9' ;

fragment
F_Digit: '0'..'9' ;

fragment
F_HexDigit: F_Digit | 'a'..'f' | 'A'..'F' ;

fragment
F_IPv4Part: F_Digit
          | F_PositiveDigit F_Digit
          | '1' F_Digit F_Digit
          | '2' ('0'..'4') F_Digit
          | '2' '5' ('0'..'5')
          ;

fragment
F_IPv4Address: F_IPv4Part '.' F_IPv4Part '.' F_IPv4Part '.' F_IPv4Part ;

fragment
F_IPv4Prefix: F_Digit
            | ('1'..'2') F_Digit
            | '3' ('0'..'2')
            ;

fragment
F_Letter: 'a'..'z' | 'A'..'Z' ;

fragment
F_AlphaNum: F_Letter | F_Digit ;
