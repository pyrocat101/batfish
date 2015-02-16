package batfish.filter.ast;

import batfish.filter.BPF;
import batfish.filter.ast.node.Expr;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ASTBuilderTest {
    private ASTBuilder astBuilder;

    @Before
    public void setUp() {
        this.astBuilder = new ASTBuilder();
    }

    @Test
    public void testSingleton() {
        Expr expr = BPF.getParser("ip src host 127.0.0.1").expr().accept(astBuilder);
        assertEquals("ip src host 127.0.0.1", expr.toString());
    }

    @Test
    public void testParenExpr() {
        Expr expr = BPF.getParser("(ip)").expr().accept(astBuilder);
        assertEquals("ip", expr.toString());
    }

    @Test
    public void testNegation() {
        String expected = "!(ip)";
        Expr expr1 = BPF.getParser("!ip").expr().accept(astBuilder);
        Expr expr2 = BPF.getParser("not ip").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    @Test
    public void testDisjunction() {
        String expected = "(tcp) || (udp)";
        Expr expr1 = BPF.getParser("tcp || udp").expr().accept(astBuilder);
        Expr expr2 = BPF.getParser("tcp or udp").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    @Test
    public void testConjunction() {
        String expected = "(tcp) && (udp)";
        Expr expr1 = BPF.getParser("tcp && udp").expr().accept(astBuilder);
        Expr expr2 = BPF.getParser("tcp and udp").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    @Test
    public void testProtoOnly() {
        Expr expr = BPF.getParser("ip").expr().accept(astBuilder);
        assertEquals("ip", expr.toString());
    }

    @Test
    public void testIdOnly() {
        try {
            BPF.getParser("80").expr().accept(astBuilder);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("invalid syntax (require qualifiers)"));
        }
        BPF.getParser("tcp dst port 80").expr().accept(astBuilder);
        Expr expr = BPF.getParser("443").expr().accept(astBuilder);
        assertEquals("if qualifiers are omitted, they are assumed to be those of last filter parsed",
                "tcp dst port 443", expr.toString());
    }

    @Test
    public void testProg() {
        Expr expr = BPF.getParser("tcp dst port 80").prog().accept(astBuilder);
        assertEquals("tcp dst port 80", expr.toString());
    }

    @Test
    public void testPDQualId() {
        Expr expr = BPF.getParser("tcp src and dst 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if type qualifier is omitted, it is assumed to be host",
                "tcp src and dst host 127.0.0.1",
                expr.toString());
    }

    @Test
    public void testPDTQualId() {
        Expr expr = BPF.getParser("tcp src net 192.168").expr().accept(astBuilder);
        assertEquals("tcp src net 192.168.0.0/16", expr.toString());
    }

    @Test
    public void testDQualId() {
        Expr expr = BPF.getParser("src and dst 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if protocol and type qualifiers are omitted, " +
                        "they are assumed to be host type with compatible protocol",
                "src and dst host 127.0.0.1", expr.toString());
    }

    @Test
    public void testDTQualId() {
        Expr expr = BPF.getParser("src and dst portrange 1000-1337").expr().accept(astBuilder);
        assertEquals("if protocol qualifier is omitted, it is assumed to be a compatible protocol",
                "src and dst portrange 1000-1337",
                expr.toString());
    }

    @Test
    public void testPQualId() {
        Expr expr = BPF.getParser("tcp 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if direction and type qualifiers are omitted, they are assumed to be" +
                        "src or dst direction and host type",
                "tcp src or dst host 127.0.0.1",
                expr.toString());
    }

    @Test
    public void testPTQualId() {
        Expr expr = BPF.getParser("tcp port 80").expr().accept(astBuilder);
        assertEquals("if direction qualifier is omitted, it is assumed to be src or dst",
                "tcp src or dst port 80",
                expr.toString());
    }

    @Test
    public void testTQualId() {
        Expr expr = BPF.getParser("tcp src 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if type qualifier is omitted, it is assumed to be host",
                "tcp src host 127.0.0.1",
                expr.toString());
    }

    @Test(expected = RecognitionException.class)
    public void testInvalidQualifier() {
        BPF.getParser("not (src and dst udp or icmp)").expr().accept(astBuilder);
    }
}
