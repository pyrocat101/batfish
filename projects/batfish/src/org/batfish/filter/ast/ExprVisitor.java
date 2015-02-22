package org.batfish.filter.ast;

import org.batfish.filter.ast.node.Atom;
import org.batfish.filter.ast.node.Conjunction;
import org.batfish.filter.ast.node.Disjunction;
import org.batfish.filter.ast.node.Negation;

public interface ExprVisitor<T> {
    public T visit(Atom atom);
    public T visit(Disjunction disjunction);
    public T visit(Conjunction conjunction);
    public T visit(Negation negation);
}
