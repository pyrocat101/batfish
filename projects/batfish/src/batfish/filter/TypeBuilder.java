package batfish.filter;

import batfish.filter.node.Type;
import batfish.grammar.bpf.BPFParser;
import batfish.grammar.bpf.BPFParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

public class TypeBuilder extends BPFParserBaseVisitor<Type> {
    @Override
    public Type visitTypeHost(@NotNull BPFParser.TypeHostContext ctx) {
        return Type.HOST;
    }

    @Override
    public Type visitTypeNet(@NotNull BPFParser.TypeNetContext ctx) {
        return Type.NET;
    }

    @Override
    public Type visitTypePort(@NotNull BPFParser.TypePortContext ctx) {
        return Type.PORT;
    }

    @Override
    public Type visitTypePortRange(@NotNull BPFParser.TypePortRangeContext ctx) {
        return Type.PORT_RANGE;
    }
}
