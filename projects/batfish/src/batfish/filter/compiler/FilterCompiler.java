package batfish.filter.compiler;

import batfish.filter.ast.node.*;
import batfish.z3.node.*;

import java.util.Arrays;

public class FilterCompiler {
    private static SrcIDCompiler srcIDCompiler = new SrcIDCompiler();
    private static DstIDCompiler dstIDCompiler = new DstIDCompiler();

    public static VarIntExpr IP_PROT = new VarIntExpr("ip_prot");
    public static int IP_PROT_NBIT = 9;

    public static LitIntExpr protoNumber(int num) {
        return new LitIntExpr(num, IP_PROT_NBIT);
    }

    /**
     * compile({proto, dir, type}) = compile(proto) && compile(dir, type)
     */
    public static BooleanExpr compile(Filter filter) {
        BooleanExpr proto = compile(filter.protocol);
        BooleanExpr id = compile(filter.direction, filter.id);
        return new AndExpr(Arrays.asList(proto, id));
    }

    /**
     * compile(proto) = "(= ip_prot {proto_number})"
     */
    public static BooleanExpr compile(Protocol protocol) {
        // IP protocol numbers:
        // ICMP - 1
        // IGMP - 2
        // TCP - 6
        // UDP - 17
        // SCTP - 132
        switch (protocol) {
            case IP:
                return TrueExpr.INSTANCE;
            case ICMP:
                return new EqExpr(IP_PROT, protoNumber(1));
            case IGMP:
                return new EqExpr(IP_PROT, protoNumber(2));
            case TCP:
                return new EqExpr(IP_PROT, protoNumber(6));
            case UDP:
                return new EqExpr(IP_PROT, protoNumber(17));
            case SCTP:
                return new EqExpr(IP_PROT, protoNumber(132));
        }
        // make IDE happy
        return null;
    }

    /**
     * compile(dir1 and dir2, id) = compile(dir1, id) and compile(dir2, id)
     * compile(dir1 or dir2, id) = compile(dir1, id) or compile2(dir2, id)
     * compile(dir, not id) = not compile(dir, id)
     */
    public static BooleanExpr compile(Direction dir, ID id) {
        if (dir == Direction.SRC_AND_DST) {
            BooleanExpr t1 = compile(Direction.SRC, id);
            BooleanExpr t2 = compile(Direction.DST, id);
            return new AndExpr(Arrays.asList(t1, t2));
        } else if (dir == Direction.SRC_OR_DST) {
            BooleanExpr t1 = compile(Direction.SRC, id);
            BooleanExpr t2 = compile(Direction.DST, id);
            return new OrExpr(Arrays.asList(t1, t2));
        } else if (id instanceof NotID) {
            return new NotExpr(compile(dir, ((NotID) id).negated));
        } else if (dir == Direction.SRC) {
            return id.accept(srcIDCompiler);
        } else if (dir == Direction.DST) {
            return id.accept(dstIDCompiler);
        }
        assert false;
        return null;
    }
}
