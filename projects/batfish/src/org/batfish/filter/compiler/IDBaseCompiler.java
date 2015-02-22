package org.batfish.filter.compiler;

import org.batfish.filter.ast.node.*;
import org.batfish.representation.Prefix;
import org.batfish.z3.Synthesizer;
import org.batfish.z3.node.*;

import java.util.Arrays;

public abstract class IDBaseCompiler implements IDVisitor<BooleanExpr> {
    public static final int PORT_NBITS = 16;
    public static final int HOST_NBITS = 32;

    public abstract VarIntExpr getIpVar();
    public abstract VarIntExpr getPortVar();

    public static long getShiftedAddress(Prefix net) {
        return net.getAddress().asLong() >>> (HOST_NBITS - net.getPrefixLength());
    }

    @Override
    public BooleanExpr visit(Host host) {
        return new EqExpr(getIpVar(), new LitIntExpr(host.host));
    }

    @Override
    public BooleanExpr visit(Net net) {
        IntExpr lhs = new ExtractExpr(getIpVar(), HOST_NBITS - net.net.getPrefixLength(), HOST_NBITS - 1);
        IntExpr rhs = new LitIntExpr(getShiftedAddress(net.net), net.net.getPrefixLength());
        return new EqExpr(lhs, rhs);
    }

    @Override
    public BooleanExpr visit(Port port) {
        return new EqExpr(getPortVar(), new LitIntExpr(port.port, PORT_NBITS));
    }

    @Override
    public BooleanExpr visit(PortRange range) {
        BooleanExpr gte = Synthesizer.bitvectorGEExpr(getPortVar().getVariable(), range.low, PORT_NBITS);
        BooleanExpr lte = Synthesizer.bitvectorLEExpr(getPortVar().getVariable(), range.high, PORT_NBITS);
        return new AndExpr(Arrays.asList(gte, lte));
    }
}
