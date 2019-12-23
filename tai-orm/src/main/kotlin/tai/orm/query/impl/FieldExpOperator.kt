package tai.orm.query.impl

import tai.criteria.*
import tai.orm.field_
import tai.orm.query.AliasAndColumn
import tai.orm.query.ex.QueryParserException
import tai.orm.translatedTo_

class FieldExpOperator : CriteriaOperation0 {
    override val paramSpecs: Collection<ParamSpec> = listOf();

    override fun renderExpression(dialect: CriteriaDialect): CriteriaExpression {
        val aliasAndColumn = dialect.ctxObject[translatedTo_] as AliasAndColumn?
            ?: throw QueryParserException("TranslatedTo does not exists in criteria operator for " +
                    "field expression '${dialect.ctxObject[field_]}'");

        return joinCriteriaExpressions(
            listOf(
                CritExpSingle(aliasAndColumn.alias), CritExpSingle(aliasAndColumn.column)
            ), "."
        )
    }
}