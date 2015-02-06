package batfish.filter.compiler;

import batfish.filter.ast.node.*;
import batfish.z3.node.*;
import junit.framework.TestCase;

import java.util.Arrays;

public class ExprCompilerTest extends TestCase {
    private ExprCompiler compiler;

    private static final Filter tcpFilter = new Filter(Protocol.TCP);
    private static final Filter udpFilter = new Filter(Protocol.UDP);

    private static final BooleanExpr tcpExpr = new EqExpr(
            FilterCompiler.IP_PROT_VAR,
            new LitIntExpr(FilterCompiler.PROTO_TCP, FilterCompiler.IP_PROT_NBIT));
    private static final BooleanExpr udpExpr = new EqExpr(
            FilterCompiler.IP_PROT_VAR,
            new LitIntExpr(FilterCompiler.PROTO_UDP, FilterCompiler.IP_PROT_NBIT));

    @Override
    protected void setUp() throws Exception {
        this.compiler = new ExprCompiler();
    }

    public void testAtom() {
        BooleanExpr expr = new Atom(tcpFilter).accept(compiler);
        assertEquals(tcpExpr.toString(), expr.toString());
    }

    public void testConjunction() {
        BooleanExpr actual = new Conjunction(new Atom(tcpFilter), new Atom(udpFilter)).accept(compiler);
        BooleanExpr expected = new AndExpr(Arrays.asList(tcpExpr, udpExpr));
        assertEquals(expected.toString(), actual.toString());
    }

    public void testDisjunction() {
        BooleanExpr actual = new Disjunction(new Atom(tcpFilter), new Atom(udpFilter)).accept(compiler);
        BooleanExpr expected = new OrExpr(Arrays.asList(tcpExpr, udpExpr));
        assertEquals(expected.toString(), actual.toString());
    }

    public void testNegation() {
        BooleanExpr actual = new Negation(new Atom(tcpFilter)).accept(compiler);
        BooleanExpr expected = new NotExpr(tcpExpr);
        assertEquals(expected.toString(), actual.toString());
    }
}
