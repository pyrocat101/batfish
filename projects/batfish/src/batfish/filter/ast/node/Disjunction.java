package batfish.filter.ast.node;

public class Disjunction extends Expr {
    public final Expr lhs;
    public final Expr rhs;

    public Disjunction(Expr lhs, Expr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("(%s) || (%s)", lhs, rhs);
    }
}
