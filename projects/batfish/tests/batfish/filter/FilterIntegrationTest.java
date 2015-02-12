package batfish.filter;

import batfish.filter.ast.ASTBuilder;
import batfish.filter.ast.ASTBuilderTest;
import batfish.filter.compiler.ExprCompiler;
import batfish.filter.compiler.FilterCompiler;
import batfish.z3.node.*;
import junit.framework.TestCase;

import java.util.Arrays;

public class FilterIntegrationTest extends TestCase {
    private static final ASTBuilder astBuilder = new ASTBuilder();
    private static final ExprCompiler exprCompiler = new ExprCompiler();

    public static final VarIntExpr IP_PROT_VAR = new VarIntExpr("ip_prot");
    public static final VarIntExpr SRC_PORT_VAR = new VarIntExpr("src_port");
    public static final VarIntExpr DST_PORT_VAR = new VarIntExpr("dst_port");

    public static final LitIntExpr UDP_PROT = FilterCompiler.protoNumber(FilterCompiler.PROTO_UDP);

    public static BooleanExpr compile(String src) {
        return ASTBuilderTest.getParser(src).expr().accept(astBuilder).accept(exprCompiler);
    }

    public void testProtoOnly() {
        BooleanExpr expr = compile("udp");
        assertEquals(
                new EqExpr(IP_PROT_VAR, UDP_PROT).toString(),
                expr.toString());
    }

    public void testPortOnly() {
        BooleanExpr expr = compile("port telnet");
        assertEquals(
                new OrExpr(Arrays.asList(
                        (BooleanExpr)new EqExpr(SRC_PORT_VAR, new LitIntExpr(23, 16)),
                        (BooleanExpr)new EqExpr(DST_PORT_VAR, new LitIntExpr(23, 16)))).toString(),
                expr.toString());
    }

    public void testNotPort() {
        BooleanExpr expr = compile("port not telnet");
        assertEquals(
                new OrExpr(Arrays.asList(
                        (BooleanExpr)new NotExpr(new EqExpr(SRC_PORT_VAR, new LitIntExpr(23, 16))),
                        (BooleanExpr)new NotExpr(new EqExpr(DST_PORT_VAR, new LitIntExpr(23, 16))))).toString(),
                expr.toString());
    }
}
