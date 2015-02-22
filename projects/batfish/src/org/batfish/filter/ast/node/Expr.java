package org.batfish.filter.ast.node;

import org.batfish.filter.ast.ExprVisitor;

public abstract class Expr {
    public abstract <T> T accept(ExprVisitor<? extends T> visitor);
}
