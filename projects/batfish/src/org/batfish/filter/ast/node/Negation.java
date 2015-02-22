package org.batfish.filter.ast.node;

import org.batfish.filter.ast.ExprVisitor;

public class Negation extends Expr {
    public final Expr expr;

    public Negation(Expr expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return String.format("!(%s)", expr);
    }

    @Override
    public <T> T accept(ExprVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
