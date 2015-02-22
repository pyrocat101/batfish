package org.batfish.filter.ast.node;

import org.batfish.filter.ast.ExprVisitor;

public class Atom extends Expr {
    public final Filter filter;

    public Atom(Filter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return this.filter.toString();
    }

    @Override
    public <T> T accept(ExprVisitor<? extends T> visitor) {
        return visitor.visit(this);
    }
}
