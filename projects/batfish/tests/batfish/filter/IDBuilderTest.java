package batfish.filter;

import batfish.filter.node.ID;
import batfish.grammar.bpf.BPFLexer;
import batfish.grammar.bpf.BPFParser;
import junit.framework.TestCase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class IDBuilderTest extends TestCase {
    private IDBuilder idBuilder;

    @Override
    protected void setUp() throws Exception {
        this.idBuilder = new IDBuilder();
    }

    private BPFParser getParser(String text) {
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

    public void testNetwork() {
        ID id = getParser("192.168").id().accept(idBuilder);
        assertEquals("192.168.0.0/16", id.toString());
    }

    public void testNotNetwork() {
        ID id = getParser("not 192.168").id().accept(idBuilder);
        assertEquals("not 192.168.0.0/16", id.toString());
    }

    public void testNotPort() {
        ID id = getParser("not 0x50").id().accept(idBuilder);
        assertEquals("0x50 = 80", "not 80", id.toString());
    }

    public void testNotPortName() {
        ID id = getParser("not ftp").id().accept(idBuilder);
        assertEquals("not 21", id.toString());
    }

    public void testNotPortRange() {
        ID id = getParser("not 0x3E8-0x539").id().accept(idBuilder);
        assertEquals("0x3E8 = 1000, 0x539 = 1337", "not 1000-1337", id.toString());
    }

    public void testPort() {
        ID id = getParser("123").id().accept(idBuilder);
        assertEquals("123", id.toString());
    }

    public void testPortName() {
        ID id = getParser("ssh").id().accept(idBuilder);
        assertEquals("22", id.toString());
    }

    public void testPortRange() {
        ID id = getParser("1000-1337").id().accept(idBuilder);
        assertEquals("1000-1337", id.toString());
    }

    public void testNet2() {
        ID id = getParser("8.8").id().accept(idBuilder);
        assertEquals("8.8.0.0/16", id.toString());
    }

    public void testNet3() {
        ID id = getParser("8.8.8").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    public void testPrefix() {
        ID id = getParser("8.8.8.0/24").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    public void testMasked() {
        ID id = getParser("8.8.8.0 mask 255.255.255.0").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    public void testHost() {
        ID id = getParser("8.8.8.8").id().accept(idBuilder);
        assertEquals("8.8.8.8", id.toString());
    }

    public void testParenPortNum() {
        ID id = getParser("(80)").id().accept(idBuilder);
        assertEquals("80", id.toString());
    }

    public void testParenPortNumRange() {
        ID id = getParser("(1000-1337)").id().accept(idBuilder);
        assertEquals("1000-1337", id.toString());
    }
}
