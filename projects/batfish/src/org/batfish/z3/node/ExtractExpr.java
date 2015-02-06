package org.batfish.z3.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExtractExpr extends IntExpr implements ComplexExpr {

   private List<Expr> _subExpressions;
   private IntExpr _var;

   public ExtractExpr(IntExpr var, int low, int high) {
      _subExpressions = new ArrayList<Expr>();
      ListExpr listExpr = new CollapsedListExpr();
      listExpr.addSubExpression(new IdExpr("_"));
      listExpr.addSubExpression(new IdExpr("extract"));
      listExpr.addSubExpression(new IdExpr(Integer.toString(high)));
      listExpr.addSubExpression(new IdExpr(Integer.toString(low)));
      _subExpressions.add(listExpr);
      _var = var;
      _subExpressions.add(_var);
      _printer = new CollapsedComplexExprPrinter(this);
   }

   public ExtractExpr(String var, int low, int high) {
      this(new VarIntExpr(var), low, high);
   }

   @Override
   public List<Expr> getSubExpressions() {
      return _subExpressions;
   }

   @Override
   public Set<String> getVariables() {
      return _var.getVariables();
   }

}
