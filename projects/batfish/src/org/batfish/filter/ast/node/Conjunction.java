package org.batfish.filter.ast.node;

import org.batfish.filter.ast.ExprVisitor;

public class Conjunction extends Expr {
    public final Expr lhs;
    public final Expr rhs;

    public Conjunction(Expr lhs, Expr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("(%s) && (%s)", lhs, rhs);
    }

    @Override
    public <T> T accept(ExprVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
