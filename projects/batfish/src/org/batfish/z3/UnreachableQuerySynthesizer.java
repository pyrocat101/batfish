package org.batfish.z3;

import org.batfish.z3.node.*;

public class UnreachableQuerySynthesizer implements QuerySynthesizer {

    private String _queryText;

    public UnreachableQuerySynthesizer(String originationNode, BooleanExpr filter) {
        OriginateExpr originate = new OriginateExpr(originationNode);
        RuleExpr injectSymbolicPackets = new RuleExpr(originate);
        AndExpr queryConditions = new AndExpr();
        queryConditions.addConjunct(DropExpr.INSTANCE);
        queryConditions.addConjunct(SaneExpr.INSTANCE);
        if (filter != null) {
            queryConditions.addConjunct(filter);
        }
        RuleExpr queryRule = new RuleExpr(queryConditions,
                QueryRelationExpr.INSTANCE);
        QueryExpr query = new QueryExpr(QueryRelationExpr.INSTANCE);
        StringBuilder sb = new StringBuilder();
        injectSymbolicPackets.print(sb, 0);
        sb.append("\n");
        queryRule.print(sb, 0);
        sb.append("\n");
        query.print(sb, 0);
        sb.append("\n");
        String queryText = sb.toString();
        _queryText = queryText;

    }

    @Override
    public String getQueryText() {
        return _queryText;
    }

}