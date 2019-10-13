package tai.criteria.operators

import tai.criteria.*

fun createGenericBiOperator(operatorSymbol: String): CriteriaOperation2 {
    return object : CriteriaOperation2 {
        override val paramSpecs: Collection<ParamSpec> = listOf(
            ParamSpecSingle(arg1_), ParamSpecSingle(arg2_)
        )

        override fun renderExpression(
            dialect: CriteriaDialect,
            param1: CriteriaExpression,
            param2: CriteriaExpression
        ): CriteriaExpression {
            return withParenthesis(
                CriteriaExpressionBuilderImpl().add(param1).add(operatorSymbol).add(param2).build()
            )
        }
    }
}

fun createComparisionOperator(operatorSymbol: String): CriteriaOperation2 {
    return object : CriteriaOperation2 {
        override val paramSpecs: Collection<ParamSpec> = listOf(
            ParamSpecSingle(arg1_), ParamSpecSingle(arg2_)
        )

        override fun renderExpression(
            dialect: CriteriaDialect,
            param1: CriteriaExpression,
            param2: CriteriaExpression
        ): CriteriaExpression {
            val modifier = dialect.ctxObject[modifier_] as ComparisonModifier?
            val parenthesis = dialect.ctxObject[is_parenthesis_] as Boolean? ?: true;

            return withParenthesis(
                if (modifier != null) {
                    CriteriaExpressionBuilderImpl().add(param1).add(operatorSymbol).add(" ").add(modifier.value)
                        .add(" ").add(param2).build()
                } else {
                    CriteriaExpressionBuilderImpl().add(param1).add(operatorSymbol).add(param2).build()
                },
                parenthesis
            )
        }
    }
}

class PlusOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, combineMulti = ::combineMulti)
    );

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " + ");
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return withParenthesis(param)
    }
}

class MultiplyOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, combineMulti = ::combineMulti)
    );

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " * ");
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return withParenthesis(param);
    }
}