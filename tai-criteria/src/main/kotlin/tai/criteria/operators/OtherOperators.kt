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
        val spaceSeparated = dialect.ctxObject.getBoolean(space_separated_);
        if (alias == null) {
            return param;
        }
        return CriteriaExpressionBuilderImpl()
            .add(param).add(if (spaceSeparated == true) " " else " AS ").add(alias).build()
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

class IsOperator : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_), ParamSpecSingle(arg2_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression
    ): CriteriaExpression {
        val isNot = dialect.ctxObject[is_not_] as Boolean?
        return if (isNot != null && isNot) {
            CriteriaExpressionBuilderImpl().add(param1).add(" IS NOT ").add(param2).build()
        } else {
            CriteriaExpressionBuilderImpl().add(param1).add(" IS ").add(param2).build()
        }
    }
}

class StartOperator : CriteriaOperation0 {
    override val paramSpecs: Collection<ParamSpec> = listOf();

    override fun renderExpression(
        dialect: CriteriaDialect
    ): CriteriaExpression {
        val src = dialect.ctxObject[src_] as String? ?: return CriteriaExpressionBuilderImpl().add("*").build();
        return CriteriaExpressionBuilderImpl().add(src).add(".*").build()
    }
}