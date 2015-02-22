package batfish.filter.compiler;

import batfish.filter.ast.node.*;
import batfish.z3.node.*;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FilterCompilerTest {
    // compile({proto, dir, type}) = compile(proto) && compile(dir, type)
    @Test
    public void testProtoDirType() {
        Protocol tcp = Protocol.TCP;
        Direction dst = Direction.DST;
        Type type = Type.PORT;
        Port port = new Port(80);
        BooleanExpr actual = FilterCompiler.compile(new Filter(tcp, dst, type, port));
        BooleanExpr expected = new AndExpr(
                Arrays.asList(FilterCompiler.compile(tcp), FilterCompiler.compile(dst, port)));
        assertEquals(expected.toString(), actual.toString());
    }

    // compile(proto) = "(= ip_prot {proto_number})"
    @Test
    public void testProto() {
        Protocol tcp = Protocol.TCP;
        BooleanExpr actual = FilterCompiler.compile(tcp);
        BooleanExpr expected = new EqExpr(
                FilterCompiler.IP_PROT_VAR,
                FilterCompiler.protoNumber(FilterCompiler.PROTO_TCP));
        assertEquals(expected.toString(), actual.toString());
    }

    // compile(dir1 and dir2, id) = compile(dir1, id) and compile(dir2, id)
    @Test
    public void testSrcAndDst() {
        BooleanExpr actual = FilterCompiler.compile(Direction.SRC_AND_DST, new Port(80));
        BooleanExpr expected = new AndExpr(Arrays.asList(
                FilterCompiler.compile(Direction.SRC, new Port(80)),
                FilterCompiler.compile(Direction.DST, new Port(80))));
        assertEquals(expected.toString(), actual.toString());
    }

    // compile(dir1 or dir2, id) = compile(dir1, id) or compile(dir2, id)
    @Test
    public void testSrcOrDst() {
        BooleanExpr actual = FilterCompiler.compile(Direction.SRC_OR_DST, new Port(80));
        BooleanExpr expected = new OrExpr(Arrays.asList(
                FilterCompiler.compile(Direction.SRC, new Port(80)),
                FilterCompiler.compile(Direction.DST, new Port(80))));
        assertEquals(expected.toString(), actual.toString());
    }

    // compile(dir, not id) = not compile(dir, id)
    @Test
    public void testNotId() {
        BooleanExpr actual = FilterCompiler.compile(Direction.SRC, new NotID(new Port(80)));
        BooleanExpr expected = new NotExpr(FilterCompiler.compile(Direction.SRC, new Port(80)));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testComplex() {
        // any TCP package whose src or dst port is not 80
        BooleanExpr actual = FilterCompiler.compile(new Filter(
                Protocol.TCP, Direction.SRC_OR_DST, Type.PORT, new NotID(new Port(80))));
        BooleanExpr expected = new AndExpr(Arrays.asList(
                FilterCompiler.compile(Protocol.TCP),
                new AndExpr(Arrays.asList(
                        // invariant sub-typing because of mutable container
                        (BooleanExpr) new NotExpr(FilterCompiler.compile(Direction.SRC, new Port(80))),
                        (BooleanExpr) new NotExpr(FilterCompiler.compile(Direction.DST, new Port(80)))))));
        assertEquals(expected.simplify().toString(), actual.simplify().toString());
    }
}
