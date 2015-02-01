package batfish.filter.node;

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
}