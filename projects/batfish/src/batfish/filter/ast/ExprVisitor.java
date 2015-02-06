package batfish.filter.ast;

import batfish.filter.ast.node.Atom;
import batfish.filter.ast.node.Conjunction;
import batfish.filter.ast.node.Disjunction;
import batfish.filter.ast.node.Negation;

public interface ExprVisitor<T> {
    public T visit(Atom atom);
    public T visit(Disjunction disjunction);
    public T visit(Conjunction conjunction);
    public T visit(Negation negation);
}
