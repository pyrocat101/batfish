package org.batfish.z3.node;

public class PostInInterfaceExpr extends InterfacePacketRelExpr {

   public static final String BASE_NAME = "R_postin_interface";

   public PostInInterfaceExpr(String nodeName, String interfaceName) {
      super(BASE_NAME, nodeName, interfaceName);
   }

}
