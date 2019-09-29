package tai.criteria.operators

import tai.criteria.*

fun createOneArgFun(funName: String): CriteriaOperation1 {
    return object : CriteriaOperation1 {
        override val paramSpecs: Collection<ParamSpec> = listOf(
            ParamSpecSingle(arg_)
        );

        override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
            return CriteriaExpressionBuilderImpl()
                .add("$funName(").add(param).add(")").build();
        }
    }
}