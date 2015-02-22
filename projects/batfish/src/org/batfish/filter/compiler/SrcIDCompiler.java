package org.batfish.filter.compiler;

import org.batfish.z3.node.VarIntExpr;

public class SrcIDCompiler extends IDBaseCompiler {
    public static final VarIntExpr SRC_IP = new VarIntExpr("src_ip");
    public static final VarIntExpr SRC_PORT = new VarIntExpr("src_port");

    @Override
    public VarIntExpr getIpVar() {
        return SRC_IP;
    }

    @Override
    public VarIntExpr getPortVar() {
        return SRC_PORT;
    }
}
