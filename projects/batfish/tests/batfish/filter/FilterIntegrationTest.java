package batfish.filter;

import batfish.filter.ast.ASTBuilder;
import batfish.filter.compiler.ExprCompiler;
import batfish.filter.compiler.FilterCompiler;
import batfish.z3.node.*;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FilterIntegrationTest {
    public static final VarIntExpr IP_PROT_VAR = new VarIntExpr("ip_prot");
    public static final VarIntExpr SRC_PORT_VAR = new VarIntExpr("src_port");
    public static final VarIntExpr DST_PORT_VAR = new VarIntExpr("dst_port");
    public static final LitIntExpr UDP_PROT = FilterCompiler.protoNumber(FilterCompiler.PROTO_UDP);
    private static final ASTBuilder astBuilder = new ASTBuilder();
    private static final ExprCompiler exprCompiler = new ExprCompiler();

    public static BooleanExpr compile(String src) {
        return BPF.getParser(src).expr().accept(astBuilder).accept(exprCompiler);
    }

    @Test
    public void testProtoOnly() {
        BooleanExpr expr = compile("udp");
        assertEquals(
                new EqExpr(IP_PROT_VAR, UDP_PROT).toString(),
                expr.toString());
    }

    @Test
    public void testPortOnly() {
        BooleanExpr expr = compile("port telnet");
        assertEquals(
                new OrExpr(Arrays.asList(
                        (BooleanExpr) new EqExpr(SRC_PORT_VAR, new LitIntExpr(23, 16)),
                        (BooleanExpr) new EqExpr(DST_PORT_VAR, new LitIntExpr(23, 16)))).toString(),
                expr.toString());
    }

    @Test
    public void testNotPort() {
        BooleanExpr expr = compile("port not telnet");
        assertEquals(
                new AndExpr(Arrays.asList(
                        (BooleanExpr) new NotExpr(new EqExpr(SRC_PORT_VAR, new LitIntExpr(23, 16))),
                        (BooleanExpr) new NotExpr(new EqExpr(DST_PORT_VAR, new LitIntExpr(23, 16))))).toString(),
                expr.toString());
    }
}
