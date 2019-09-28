package tai.criteria.operators

import tai.criteria.*
import java.util.*
import kotlin.reflect.KClass

class ColumnNameOperator : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    );
    override val parameterType: KClass<String> = String::class;

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        val alias = dialect.ctxObject[alias_] as String?;
        return dialect.column(param, alias);
    }
}

class TableNameOperator : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    );
    override val parameterType: KClass<String> = String::class;

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        val alias = dialect.ctxObject[alias_] as String?;
        return dialect.table(param, alias);
    }
}

class AsOperator: CriteriaOperation1 {

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
        return CriteriaExpressionBuilderImpl().add(param).add(" AS ").add(alias!!).build()
    }
}