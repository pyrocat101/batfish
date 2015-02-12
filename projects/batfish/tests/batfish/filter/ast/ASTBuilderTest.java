package batfish.filter.ast;

import batfish.filter.ast.ASTBuilder;
import batfish.filter.ast.node.Expr;
import batfish.grammar.bpf.BPFLexer;
import batfish.grammar.bpf.BPFParser;
import junit.framework.TestCase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ASTBuilderTest extends TestCase {
    private ASTBuilder astBuilder;

    @Override
    protected void setUp() throws Exception {
        this.astBuilder = new ASTBuilder();
    }

    public static BPFParser getParser(String text) {
        InputStream input;
        ANTLRInputStream stream;
        try {
            input = IOUtils.toInputStream(text, "UTF-8");
            stream = new ANTLRInputStream(input);
        } catch (IOException e) {
            throw new RuntimeException("IO error");
        }
        BPFLexer lexer = new BPFLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new BPFParser(tokens);
    }

    public void testSingleton() {
        Expr expr = getParser("ip src host 127.0.0.1").expr().accept(astBuilder);
        assertEquals("ip src host 127.0.0.1", expr.toString());
    }

    public void testParenExpr() {
        Expr expr = getParser("(ip)").expr().accept(astBuilder);
        assertEquals("ip", expr.toString());
    }

    public void testNegation() {
        String expected = "!(ip)";
        Expr expr1 = getParser("!ip").expr().accept(astBuilder);
        Expr expr2 = getParser("not ip").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    public void testDisjunction() {
        String expected = "(tcp) || (udp)";
        Expr expr1 = getParser("tcp || udp").expr().accept(astBuilder);
        Expr expr2 = getParser("tcp or udp").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    public void testConjunction() {
        String expected = "(tcp) && (udp)";
        Expr expr1 = getParser("tcp && udp").expr().accept(astBuilder);
        Expr expr2 = getParser("tcp and udp").expr().accept(astBuilder);
        assertEquals(expected, expr1.toString());
        assertEquals(expected, expr2.toString());
    }

    public void testProtoOnly() {
        Expr expr = getParser("ip").expr().accept(astBuilder);
        assertEquals("ip", expr.toString());
    }

    public void testIdOnly() {
        try {
            getParser("80").expr().accept(astBuilder);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("invalid syntax (require qualifiers)"));
        }
        getParser("tcp dst port 80").expr().accept(astBuilder);
        Expr expr = getParser("443").expr().accept(astBuilder);
        assertEquals("if qualifiers are omitted, they are assumed to be those of last filter parsed",
                "tcp dst port 443", expr.toString());
    }

    public void testProg() {
        Expr expr = getParser("tcp dst port 80").prog().accept(astBuilder);
        assertEquals("tcp dst port 80", expr.toString());
    }

    public void testPDQualId() {
        Expr expr = getParser("tcp src and dst 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if type qualifier is omitted, it is assumed to be host",
                "tcp src and dst host 127.0.0.1",
                expr.toString());
    }

    public void testPDTQualId() {
        Expr expr = getParser("tcp src net 192.168").expr().accept(astBuilder);
        assertEquals("tcp src net 192.168.0.0/16", expr.toString());
    }

    public void testDQualId() {
        Expr expr = getParser("src and dst 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if protocol and type qualifiers are omitted, " +
                     "they are assumed to be host type with compatible protocol",
                "src and dst host 127.0.0.1", expr.toString());
    }

    public void testDTQualId() {
        Expr expr = getParser("src and dst portrange 1000-1337").expr().accept(astBuilder);
        assertEquals("if protocol qualifier is omitted, it is assumed to be a compatible protocol",
                "src and dst portrange 1000-1337",
                expr.toString());
    }

    public void testPQualId() {
        Expr expr = getParser("tcp 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if direction and type qualifiers are omitted, they are assumed to be" +
                     "src or dst direction and host type",
                "tcp src or dst host 127.0.0.1",
                expr.toString());
    }

    public void testPTQualId() {
        Expr expr = getParser("tcp port 80").expr().accept(astBuilder);
        assertEquals("if direction qualifier is omitted, it is assumed to be src or dst",
                "tcp src or dst port 80",
                expr.toString());
    }

    public void testTQualId() {
        Expr expr = getParser("tcp src 127.0.0.1").expr().accept(astBuilder);
        assertEquals("if type qualifier is omitted, it is assumed to be host",
                "tcp src host 127.0.0.1",
                expr.toString());
    }
}
