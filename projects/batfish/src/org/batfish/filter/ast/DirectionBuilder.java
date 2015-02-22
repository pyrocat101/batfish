package org.batfish.filter.ast;

import org.batfish.filter.ast.node.Direction;
import org.batfish.grammar.bpf.BPFParser;
import org.batfish.grammar.bpf.BPFParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

public class DirectionBuilder extends BPFParserBaseVisitor<Direction> {
    @Override
    public Direction visitDirDst(@NotNull BPFParser.DirDstContext ctx) {
        return Direction.DST;
    }

    @Override
    public Direction visitDirDstAndSrc(@NotNull BPFParser.DirDstAndSrcContext ctx) {
        return Direction.SRC_AND_DST;
    }

    @Override
    public Direction visitDirDstOrSrc(@NotNull BPFParser.DirDstOrSrcContext ctx) {
        return Direction.SRC_OR_DST;
    }

    @Override
    public Direction visitDirSrc(@NotNull BPFParser.DirSrcContext ctx) {
        return Direction.SRC;
    }

    @Override
    public Direction visitDirSrcAndDst(@NotNull BPFParser.DirSrcAndDstContext ctx) {
        return Direction.SRC_AND_DST;
    }

    @Override
    public Direction visitDirSrcOrDst(@NotNull BPFParser.DirSrcOrDstContext ctx) {
        return Direction.SRC_OR_DST;
    }
}
