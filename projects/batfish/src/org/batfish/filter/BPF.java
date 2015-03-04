package org.batfish.filter;

import org.batfish.filter.ast.ASTBuilder;
import org.batfish.filter.ast.node.Expr;
import org.batfish.filter.compiler.ExprCompiler;
import org.batfish.grammar.bpf.BPFLexer;
import org.batfish.grammar.bpf.BPFParser;
import org.batfish.grammar.bpf.SyntaxErrorListener;
import org.batfish.z3.node.BooleanExpr;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class BPF {
    public static BPFParser getParser(String src) {
        InputStream input;
        ANTLRInputStream stream;
        try {
            input = IOUtils.toInputStream(src, "UTF-8");
            stream = new ANTLRInputStream(input);
        } catch (IOException e) {
            throw new RuntimeException("IO error");
        }
        BPFLexer lexer = new BPFLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BPFParser parser = new BPFParser(tokens);
        // suppress stderr output
        parser.removeErrorListeners();
        parser.addErrorListener(new SyntaxErrorListener());
        return parser;
    }

    public static BooleanExpr compile(String src) {
        if (src == null)
            return null;
        return compile(getParser(src));
    }

    public static BooleanExpr compile(BPFParser parser) {
        Expr expr = parser.prog().accept(new ASTBuilder());
        if (expr == null) {
            return null;
        }
        return expr.accept(new ExprCompiler());
    }
}
