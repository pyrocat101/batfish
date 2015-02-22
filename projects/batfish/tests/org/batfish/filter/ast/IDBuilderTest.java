package org.batfish.filter.ast;

import org.batfish.filter.BPF;
import org.batfish.filter.ast.node.ID;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IDBuilderTest {
    private IDBuilder idBuilder;

    @Before
    public void setUp() {
        this.idBuilder = new IDBuilder();
    }

    @Test
    public void testNetwork() {
        ID id = BPF.getParser("192.168").id().accept(idBuilder);
        assertEquals("192.168.0.0/16", id.toString());
    }

    @Test
    public void testNotNetwork() {
        ID id = BPF.getParser("not 192.168").id().accept(idBuilder);
        assertEquals("not 192.168.0.0/16", id.toString());
    }

    @Test
    public void testNotPort() {
        ID id = BPF.getParser("not 0x50").id().accept(idBuilder);
        assertEquals("0x50 = 80", "not 80", id.toString());
    }

    @Test
    public void testNotPortName() {
        ID id = BPF.getParser("not ftp").id().accept(idBuilder);
        assertEquals("not 21", id.toString());
    }

    @Test
    public void testNotPortRange() {
        ID id = BPF.getParser("not 0x3E8-0x539").id().accept(idBuilder);
        assertEquals("0x3E8 = 1000, 0x539 = 1337", "not 1000-1337", id.toString());
    }

    @Test
    public void testPort() {
        ID id = BPF.getParser("123").id().accept(idBuilder);
        assertEquals("123", id.toString());
    }

    @Test
    public void testPortName() {
        ID id = BPF.getParser("ssh").id().accept(idBuilder);
        assertEquals("22", id.toString());
    }

    @Test
    public void testPortRange() {
        ID id = BPF.getParser("1000-1337").id().accept(idBuilder);
        assertEquals("1000-1337", id.toString());
    }

    @Test
    public void testNet2() {
        ID id = BPF.getParser("8.8").id().accept(idBuilder);
        assertEquals("8.8.0.0/16", id.toString());
    }

    @Test
    public void testNet3() {
        ID id = BPF.getParser("8.8.8").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    @Test
    public void testPrefix() {
        ID id = BPF.getParser("8.8.8.0/24").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    @Test
    public void testMasked() {
        ID id = BPF.getParser("8.8.8.0 mask 255.255.255.0").id().accept(idBuilder);
        assertEquals("8.8.8.0/24", id.toString());
    }

    @Test
    public void testHost() {
        ID id = BPF.getParser("8.8.8.8").id().accept(idBuilder);
        assertEquals("8.8.8.8", id.toString());
    }

    @Test
    public void testParenPortNum() {
        ID id = BPF.getParser("(80)").id().accept(idBuilder);
        assertEquals("80", id.toString());
    }

    @Test
    public void testParenPortNumRange() {
        ID id = BPF.getParser("(1000-1337)").id().accept(idBuilder);
        assertEquals("1000-1337", id.toString());
    }
}
