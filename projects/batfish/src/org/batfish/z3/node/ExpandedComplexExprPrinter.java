package org.batfish.z3.node;

import java.util.List;

public class ExpandedComplexExprPrinter extends ComplexExprPrinter {

   public ExpandedComplexExprPrinter(ComplexExpr expr) {
      super(expr);
   }

   @Override
   public void print(StringBuilder sb, int indent) {
      List<Expr> subExpressions = _expr.getSubExpressions();
      sb.append("(");
      int size = subExpressions.size();
      if (size > 0) {
         subExpressions.get(0).print(sb, indent);
         for (int i = 1; i < size; i++) {
            sb.append("\n");
            for (int j = 0; j <= indent; j++) {
               sb.append(" ");
            }
            subExpressions.get(i).print(sb, indent + 1);
         }
         Expr lastSubExpression = subExpressions.get(size - 1);
         if (lastSubExpression instanceof ComplexExpr) {
            sb.append(" ");
         }
      }
      sb.append(")");
   }

}
