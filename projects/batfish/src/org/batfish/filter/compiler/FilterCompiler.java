package org.batfish.filter.compiler;

import org.batfish.filter.ast.node.*;
import org.batfish.z3.node.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterCompiler {
    private static final SrcIDCompiler srcIDCompiler = new SrcIDCompiler();
    private static final DstIDCompiler dstIDCompiler = new DstIDCompiler();

    public static final VarIntExpr IP_PROT_VAR = new VarIntExpr("ip_prot");
    public static final int IP_PROT_NBIT = 9;

    // IP protocol numbers:
    // ICMP - 1
    // IGMP - 2
    // TCP - 6
    // UDP - 17
    // SCTP - 132
    public static final int PROTO_ICMP = 1;
    public static final int PROTO_IGMP = 2;
    public static final int PROTO_TCP = 6;
    public static final int PROTO_UDP = 17;
    public static final int PROTO_SCTP = 132;

    public static LitIntExpr protoNumber(int num) {
        return new LitIntExpr(num, IP_PROT_NBIT);
    }

    /**
     * compile({proto, dir, type}) = compile(proto) && compile(dir, type)
     */
    public static BooleanExpr compile(Filter filter) {
        List<BooleanExpr> terms = new ArrayList<BooleanExpr>();
        if (filter.protocol != null) {
            terms.add(compile(filter.protocol));
        }
        if (filter.id != null) {
            terms.add(compile(filter.direction, filter.id));
        }
        return new AndExpr(terms).simplify();
    }

    /**
     * compile(proto) = "(= ip_prot {proto_number})"
     */
    public static BooleanExpr compile(Protocol protocol) {
        switch (protocol) {
            case IP:
                return TrueExpr.INSTANCE;
            case ICMP:
                return new EqExpr(IP_PROT_VAR, protoNumber(PROTO_ICMP));
            case IGMP:
                return new EqExpr(IP_PROT_VAR, protoNumber(PROTO_IGMP));
            case TCP:
                return new EqExpr(IP_PROT_VAR, protoNumber(PROTO_TCP));
            case UDP:
                return new EqExpr(IP_PROT_VAR, protoNumber(PROTO_UDP));
            case SCTP:
                return new EqExpr(IP_PROT_VAR, protoNumber(PROTO_SCTP));
        }
        // make IDE happy
        return null;
    }

    /**
     * compile(dir1 and dir2, not id) = compile(dir1, not id) or compile(dir2, not id)
     * compile(dir1 or dir2, not id) = compile(dir1, not id) and compile(dir2, not id)
     * compile(dir1 and dir2, id) = compile(dir1, id) and compile(dir2, id)
     * compile(dir1 or dir2, id) = compile(dir1, id) or compile2(dir2, id)
     * compile(dir, not id) = not compile(dir, id)
     */
    public static BooleanExpr compile(Direction dir, ID id) {
        if (dir == Direction.SRC_AND_DST) {
            BooleanExpr t1 = compile(Direction.SRC, id);
            BooleanExpr t2 = compile(Direction.DST, id);
            if (id instanceof NotID) {
                return new OrExpr(Arrays.asList(t1, t2));
            } else {
                return new AndExpr(Arrays.asList(t1, t2));
            }
        } else if (dir == Direction.SRC_OR_DST) {
            BooleanExpr t1 = compile(Direction.SRC, id);
            BooleanExpr t2 = compile(Direction.DST, id);
            if (id instanceof NotID) {
                return new AndExpr(Arrays.asList(t1, t2));
            } else {
                return new OrExpr(Arrays.asList(t1, t2));
            }
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
