package batfish.filter;

import batfish.filter.node.*;
import batfish.grammar.bpf.BPFParser;
import batfish.grammar.bpf.BPFParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

public class ASTBuilder extends BPFParserBaseVisitor<Expr> {
    private Filter lastFilter;
    private static ProtocolBuilder protocolBuilder = new ProtocolBuilder();
    private static DirectionBuilder directionBuilder = new DirectionBuilder();
    private static TypeBuilder typeBuilder = new TypeBuilder();
    private static IDBuilder idBuilder = new IDBuilder();

    public ASTBuilder() {
        this.lastFilter = null;
    }

    @Override
    public Expr visitSingleton(@NotNull BPFParser.SingletonContext ctx) {
        return ctx.term().accept(this);
    }

    @Override
    public Expr visitParenExpr(@NotNull BPFParser.ParenExprContext ctx) {
        return ctx.expr().accept(this);
    }

    @Override
    public Expr visitNegation(@NotNull BPFParser.NegationContext ctx) {
        return new Negation(ctx.term().accept(this));
    }

    @Override
    public Expr visitDisjunction(@NotNull BPFParser.DisjunctionContext ctx) {
        return new Disjunction(ctx.expr().accept(this), ctx.term().accept(this));
    }

    @Override
    public Expr visitConjunction(@NotNull BPFParser.ConjunctionContext ctx) {
        return new Conjunction(ctx.expr().accept(this), ctx.term().accept(this));
    }

    @Override
    public Expr visitProtoOnly(@NotNull BPFParser.ProtoOnlyContext ctx) {
        Protocol proto = ctx.pqual().accept(protocolBuilder);
        Filter filter = new Filter(proto);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitIdOnly(@NotNull BPFParser.IdOnlyContext ctx) {
        if (this.lastFilter == null) {
            throw new RuntimeException("invalid syntax (require qualifiers)");
        }
        ID id = ctx.id().accept(idBuilder);
        Protocol proto = this.lastFilter.protocol;
        Direction dir = this.lastFilter.direction;
        Type type = this.lastFilter.type;
        Filter filter = new Filter(proto, dir, type, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitProg(@NotNull BPFParser.ProgContext ctx) {
        if (ctx.expr() == null) {
            return null;
        } else {
            return ctx.expr().accept(this);
        }
    }

    private Filter createFilter(Protocol proto, Direction dir, Type type, ID id) {
        // for now, protocol consistency with rest qualifier need not be checked
        // if dir is missing, assume dir or dst
        if (dir == null) {
            dir = Direction.SRC_OR_DST;
        }
        // if type is missing, assume host
        if (type == null) {
            type = Type.HOST;
        }
        return new Filter(proto, dir, type, id);
    }

    @Override
    public Expr visitPDQualId(@NotNull BPFParser.PDQualIdContext ctx) {
        Protocol proto = ctx.pqual().accept(protocolBuilder);
        Direction dir = ctx.dqual().accept(directionBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(proto, dir, null, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitPDTQualId(@NotNull BPFParser.PDTQualIdContext ctx) {
        Protocol proto = ctx.pqual().accept(protocolBuilder);
        Direction dir = ctx.dqual().accept(directionBuilder);
        Type type = ctx.tqual().accept(typeBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(proto, dir, type, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitDQualId(@NotNull BPFParser.DQualIdContext ctx) {
        Direction dir = ctx.dqual().accept(directionBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(null, dir, null, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitDTQualId(@NotNull BPFParser.DTQualIdContext ctx) {
        Direction dir = ctx.dqual().accept(directionBuilder);
        Type type = ctx.tqual().accept(typeBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(null, dir, type, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitPQualId(@NotNull BPFParser.PQualIdContext ctx) {
        Protocol proto = ctx.pqual().accept(protocolBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(proto, null, null, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitPTQualId(@NotNull BPFParser.PTQualIdContext ctx) {
        Protocol proto = ctx.pqual().accept(protocolBuilder);
        Type type = ctx.tqual().accept(typeBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(proto, null, type, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }

    @Override
    public Expr visitTQualId(@NotNull BPFParser.TQualIdContext ctx) {
        Type type = ctx.tqual().accept(typeBuilder);
        ID id = ctx.id().accept(idBuilder);
        Filter filter = this.createFilter(null, null, type, id);
        this.lastFilter = filter;
        return new Atom(filter);
    }
}
