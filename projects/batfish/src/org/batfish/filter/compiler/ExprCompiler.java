package org.batfish.filter.compiler;

import org.batfish.filter.ast.ExprVisitor;
import org.batfish.filter.ast.node.Atom;
import org.batfish.filter.ast.node.Conjunction;
import org.batfish.filter.ast.node.Disjunction;
import org.batfish.filter.ast.node.Negation;
import org.batfish.z3.node.AndExpr;
import org.batfish.z3.node.BooleanExpr;
import org.batfish.z3.node.NotExpr;
import org.batfish.z3.node.OrExpr;

import java.util.Arrays;

public class ExprCompiler implements ExprVisitor<BooleanExpr> {
    @Override
    public BooleanExpr visit(Atom atom) {
        return FilterCompiler.compile(atom.filter);
    }

    @Override
    public BooleanExpr visit(Conjunction conjunction) {
        BooleanExpr lhs = conjunction.lhs.accept(this);
        BooleanExpr rhs = conjunction.rhs.accept(this);
        return new AndExpr(Arrays.asList(lhs, rhs));
    }

    @Override
    public BooleanExpr visit(Disjunction disjunction) {
        BooleanExpr lhs = disjunction.lhs.accept(this);
        BooleanExpr rhs = disjunction.rhs.accept(this);
        return new OrExpr(Arrays.asList(lhs, rhs));
    }

    @Override
    public BooleanExpr visit(Negation negation) {
        return new NotExpr(negation.expr.accept(this));
    }
}
