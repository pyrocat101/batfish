package batfish.filter.node;

public class Negation extends Expr {
    public final Expr expr;

    public Negation(Expr expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return String.format("!(%s)", expr);
    }
}
