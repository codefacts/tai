package tai.criteria.operators

import tai.base.getBoolean
import tai.criteria.*
import java.util.*

class AsOperator : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        val alias = dialect.ctxObject[alias_] as String?;
        Objects.requireNonNull(alias, "Alias is missing in AS operator");
        val spaceSeparated = dialect.ctxObject.getBoolean(space_separated_);
        return CriteriaExpressionBuilderImpl().add(param).add(if (spaceSeparated == true) " " else " AS ").add(alias!!)
            .build()
    }
}

class DistinctOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("DISTINCT(").add(param).add(")").build();
    }
}

class IsNullOperator : CriteriaOperation0 {
    override val paramSpecs: Collection<ParamSpec> = listOf();

    override fun renderExpression(dialect: CriteriaDialect): CriteriaExpression {
        val isNot = dialect.ctxObject[is_not_] as Boolean?
        if (isNot != null && isNot) {
            return CriteriaExpressionBuilderImpl().add("IS NOT ").add(dialect.nullExpression()).build()
        } else {
            return CriteriaExpressionBuilderImpl().add("IS ").add(dialect.nullExpression()).build()
        }
    }
}