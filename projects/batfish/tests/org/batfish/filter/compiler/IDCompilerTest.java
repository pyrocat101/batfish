package org.batfish.filter.compiler;

import org.batfish.filter.ast.node.Host;
import org.batfish.filter.ast.node.Net;
import org.batfish.filter.ast.node.Port;
import org.batfish.filter.ast.node.PortRange;
import org.batfish.representation.Ip;
import org.batfish.representation.Prefix;
import org.batfish.z3.Synthesizer;
import org.batfish.z3.node.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class IDCompilerTest {
    public static final VarIntExpr IP_VAR = SrcIDCompiler.SRC_IP;
    public static final VarIntExpr PORT_VAR = SrcIDCompiler.SRC_PORT;
    private IDBaseCompiler compiler;

    @Before
    public void setUp() {
        compiler = new SrcIDCompiler();
    }

    @Test
    public void testHost() {
        BooleanExpr actual = new Host("8.8.8.8").accept(compiler);
        BooleanExpr expected = new EqExpr(IP_VAR, new LitIntExpr(new Ip("8.8.8.8")));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testNet() {
        BooleanExpr actual = new Net(new Prefix("8.8.8.0/24")).accept(compiler);
        BooleanExpr expected = new EqExpr(new ExtractExpr(IP_VAR, 8, 31), new LitIntExpr(0x080808, 24));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testPort() {
        BooleanExpr actual = new Port(8080).accept(compiler);
        BooleanExpr expected = new EqExpr(PORT_VAR, new LitIntExpr(8080, IDBaseCompiler.PORT_NBITS));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testPortRange() {
        BooleanExpr actual = new PortRange(1000, 1337).accept(compiler);
        BooleanExpr expected = new AndExpr(Arrays.asList(
                Synthesizer.bitvectorGEExpr(PORT_VAR.getVariable(), 1000, IDBaseCompiler.PORT_NBITS),
                Synthesizer.bitvectorLEExpr(PORT_VAR.getVariable(), 1337, IDBaseCompiler.PORT_NBITS)));
        assertEquals(expected.toString(), actual.toString());
    }
}
