package org.batfish.filter.compiler;

import org.batfish.z3.node.VarIntExpr;

public class DstIDCompiler extends IDBaseCompiler {
    public static final VarIntExpr DST_IP = new VarIntExpr("dst_ip");
    public static final VarIntExpr DST_PORT = new VarIntExpr("dst_port");

    @Override
    public VarIntExpr getIpVar() {
        return DST_IP;
    }

    @Override
    public VarIntExpr getPortVar() {
        return DST_PORT;
    }
}
