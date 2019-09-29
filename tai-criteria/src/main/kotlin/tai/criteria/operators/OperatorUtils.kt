package tai.criteria.operators

import tai.criteria.CriteriaDialect
import tai.criteria.CriteriaExpression
import tai.criteria.joinCriteriaExpressions

internal fun combineOrderBy(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
    return joinCriteriaExpressions(list, ", ");
}

internal fun combineGroupBy(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
    return joinCriteriaExpressions(list, ", ");
}

internal fun combineJoin(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
    return joinCriteriaExpressions(list, ", ");
}

internal fun combineFrom(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
    return joinCriteriaExpressions(list, ", ")
}

internal fun combineSelect(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
    return joinCriteriaExpressions(list, ", ");
}