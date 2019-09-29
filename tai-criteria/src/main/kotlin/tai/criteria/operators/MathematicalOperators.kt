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
            return withParenthesis(CriteriaExpressionBuilderImpl()) {
                    expBuilder -> expBuilder.add(param1).add(operatorSymbol).add(param2);
            }.build();
        }
    }
}

class PlusOperator: CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, combineMulti = ::combineMulti)
    );

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " + ");
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) return param;
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
            expBuilder -> expBuilder.add(param)
        }.build();
    }
}

class MultiplyOperator: CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, combineMulti = ::combineMulti)
    );

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " * ");
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) return param;
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
                expBuilder -> expBuilder.add(param)
        }.build();
    }
}