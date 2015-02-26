package org.batfish.grammar.bpf;

import org.antlr.v4.runtime.*;
import org.batfish.filter.PacketFilterException;

public class SyntaxErrorListener extends BaseErrorListener {
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("line %d:%d %s\n", line, charPositionInLine, msg));
        underlineError(sb, recognizer, (Token) offendingSymbol, line, charPositionInLine);
        throw new PacketFilterException(sb.toString());
    }

    protected void underlineError(StringBuilder sb,
                                  Recognizer recognizer,
                                  Token offendingToken, int line,
                                  int charPositionInLine) {
        CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] lines = input.split("\n");
        String errorLine = lines[line - 1];
        sb.append(errorLine);
        sb.append('\n');
        for (int i = 0; i < charPositionInLine; i++) sb.append(' ');
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if (start >= 0 && stop >= 0) {
            for (int i = start; i <= stop; i++) sb.append('^');
        }
        sb.append('\n');
    }
}
