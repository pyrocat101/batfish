package batfish.filter.ast.node;

import batfish.filter.ast.ExprVisitor;

public abstract class Expr {
    public abstract <T> T accept(ExprVisitor<? extends T> visitor);
}
