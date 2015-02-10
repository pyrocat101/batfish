package batfish.filter.compiler;

import batfish.filter.ast.node.Host;
import batfish.filter.ast.node.Net;
import batfish.filter.ast.node.Port;
import batfish.filter.ast.node.PortRange;
import batfish.representation.Ip;
import batfish.representation.Prefix;
import batfish.z3.Synthesizer;
import batfish.z3.node.*;
import junit.framework.TestCase;

import java.util.Arrays;

public class IDCompilerTest extends TestCase {
    private IDBaseCompiler compiler;

    public static final VarIntExpr IP_VAR = SrcIDCompiler.SRC_IP;
    public static final VarIntExpr PORT_VAR = SrcIDCompiler.SRC_PORT;

    @Override
    protected void setUp() throws Exception {
        compiler = new SrcIDCompiler();
    }

    public void testHost() {
        BooleanExpr actual = new Host("8.8.8.8").accept(compiler);
        BooleanExpr expected = new EqExpr(IP_VAR, new LitIntExpr(new Ip("8.8.8.8")));
        assertEquals(expected.toString(), actual.toString());
    }

    public void testNet() {
        BooleanExpr actual = new Net(new Prefix("8.8.8.0/24")).accept(compiler);
        BooleanExpr expected = new EqExpr(new ExtractExpr(IP_VAR, 31, 8), new LitIntExpr(new Ip("8.8.8.0")));
        assertEquals(expected.toString(), actual.toString());
    }

    public void testPort() {
        BooleanExpr actual = new Port(8080).accept(compiler);
        BooleanExpr expected = new EqExpr(PORT_VAR, new LitIntExpr(8080, IDBaseCompiler.PORT_NBITS));
        assertEquals(expected.toString(), actual.toString());
    }

    public void testPortRange() {
        BooleanExpr actual = new PortRange(1000, 1337).accept(compiler);
        BooleanExpr expected = new AndExpr(Arrays.asList(
                Synthesizer.bitvectorGEExpr(PORT_VAR.getVariable(), 1000, IDBaseCompiler.PORT_NBITS),
                Synthesizer.bitvectorLEExpr(PORT_VAR.getVariable(), 1337, IDBaseCompiler.PORT_NBITS)));
        assertEquals(expected.toString(), actual.toString());
    }
}
