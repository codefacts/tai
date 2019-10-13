package tai.criteria

import tai.base.PrimitiveValue

interface ParamsBuilder {
    fun add(param: PrimitiveValue);
    fun build(): List<PrimitiveValue>;
}

class ParamsBuilderImpl(private val list: MutableList<PrimitiveValue>) : ParamsBuilder {
    override fun add(param: PrimitiveValue) {
        list.add(param);
    }

    override fun build(): List<PrimitiveValue> {
        return list.toList();
    }
}

val emptyCriteriaExpression = CriteriaExpressionBuilderImpl().build();

fun joinCriteriaExpressions(criteriaExpressions: List<CriteriaExpression>, delimiter: String): CriteriaExpression {
    if (criteriaExpressions.isEmpty()) return emptyCriteriaExpression;
    val builder = CriteriaExpressionBuilderImpl();
    for (i in 0..criteriaExpressions.size - 2) {
        val criteriaExpression = criteriaExpressions[i];
        if (criteriaExpression.isBlank) {
            continue;
        }
        builder.add(criteriaExpression).add(delimiter);
    }
    builder.add(criteriaExpressions[criteriaExpressions.size - 1]);
    return builder.build();
}

fun withParenthesis(expression: CriteriaExpression, isParenthesis: Boolean): CriteriaExpression {
    return when {
        expression.isBlank -> return emptyCriteriaExpression
        isParenthesis -> return CriteriaExpressionBuilderImpl().add("(").add(expression).add(")").build()
        else -> expression
    };
}

fun withParenthesis(expression: CriteriaExpression): CriteriaExpression {
    return when {
        expression.isBlank -> emptyCriteriaExpression
        else -> CriteriaExpressionBuilderImpl().add("(").add(expression).add(")").build()
    };
}

fun allExpEmpty(vararg criteriaExpressions: CriteriaExpression): Boolean {
    return criteriaExpressions.fold(true) { acc, criteriaExpression ->
        acc && criteriaExpression.isBlank
    };
}