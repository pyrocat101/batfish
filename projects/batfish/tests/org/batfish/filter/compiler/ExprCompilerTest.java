package org.batfish.filter.compiler;

import org.batfish.filter.ast.node.*;
import org.batfish.z3.node.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ExprCompilerTest {
    private static final Filter tcpFilter = new Filter(Protocol.TCP);
    private static final Filter udpFilter = new Filter(Protocol.UDP);
    private static final BooleanExpr tcpExpr = new EqExpr(
            FilterCompiler.IP_PROT_VAR,
            new LitIntExpr(FilterCompiler.PROTO_TCP, FilterCompiler.IP_PROT_NBIT));
    private static final BooleanExpr udpExpr = new EqExpr(
            FilterCompiler.IP_PROT_VAR,
            new LitIntExpr(FilterCompiler.PROTO_UDP, FilterCompiler.IP_PROT_NBIT));
    private ExprCompiler compiler;

    @Before
    public void setUp() {
        this.compiler = new ExprCompiler();
    }

    @Test
    public void testAtom() {
        BooleanExpr expr = new Atom(tcpFilter).accept(compiler);
        assertEquals(tcpExpr.toString(), expr.toString());
    }

    @Test
    public void testConjunction() {
        BooleanExpr actual = new Conjunction(new Atom(tcpFilter), new Atom(udpFilter)).accept(compiler);
        BooleanExpr expected = new AndExpr(Arrays.asList(tcpExpr, udpExpr));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDisjunction() {
        BooleanExpr actual = new Disjunction(new Atom(tcpFilter), new Atom(udpFilter)).accept(compiler);
        BooleanExpr expected = new OrExpr(Arrays.asList(tcpExpr, udpExpr));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testNegation() {
        BooleanExpr actual = new Negation(new Atom(tcpFilter)).accept(compiler);
        BooleanExpr expected = new NotExpr(tcpExpr);
        assertEquals(expected.toString(), actual.toString());
    }
}
