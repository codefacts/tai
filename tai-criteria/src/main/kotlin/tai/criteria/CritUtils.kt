package tai.criteria

interface ParamsBuilder {
    fun add(param: Any);
    fun build(): List<Any>;
}

class ParamsBuilderImpl(private val list: MutableList<Any>) : ParamsBuilder {
    override fun add(param: Any) {
        list.add(param);
    }

    override fun build(): List<Any> {
        return list.toList();
    }
}

val emptyCriteriaExpression = CriteriaExpressionBuilderImpl().build();

fun joinCriteriaExpressions(criteriaExpressions: List<CriteriaExpression>, joiner: String): CriteriaExpression {
    if (criteriaExpressions.isEmpty()) return emptyCriteriaExpression;
    val builder = CriteriaExpressionBuilderImpl();
    for (i in 0..criteriaExpressions.size - 2) {
        builder.add(criteriaExpressions[i]).add(joiner);
    }
    builder.add(criteriaExpressions[criteriaExpressions.size - 1]);
    return builder.build();
}

fun withParenthesis(expBuilder: CriteriaExpressionBuilder, exp: (expBuilder: CriteriaExpressionBuilder) -> Any): CriteriaExpressionBuilder {
    expBuilder.add("(");
    exp(expBuilder);
    expBuilder.add(")");
    return expBuilder;
}

fun allExpEmpty(vararg criteriaExpressions: CriteriaExpression): Boolean {
    return criteriaExpressions.fold(true) {
        acc, criteriaExpression -> acc && criteriaExpression.isEmpty
    };
}