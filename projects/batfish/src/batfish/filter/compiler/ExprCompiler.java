package batfish.filter.compiler;

import batfish.filter.ast.ExprVisitor;
import batfish.filter.ast.node.Atom;
import batfish.filter.ast.node.Conjunction;
import batfish.filter.ast.node.Disjunction;
import batfish.filter.ast.node.Negation;
import batfish.z3.node.AndExpr;
import batfish.z3.node.BooleanExpr;
import batfish.z3.node.NotExpr;
import batfish.z3.node.OrExpr;

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
