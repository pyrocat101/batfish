package batfish.filter.ast.node;

public class Atom extends Expr {
    public final Filter filter;

    public Atom(Filter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return this.filter.toString();
    }
}
