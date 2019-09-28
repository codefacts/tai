package tai.criteria.operators

import tai.criteria.*

class ColumnNameOperator : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    );
    override val parameterType: Class<String> = String::class.java;

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.column(param);
    }
}

class TableNameOperator : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    );
    override val parameterType: Class<String> = String::class.java;

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.table(param);
    }
}