package tai.criteria.operators

import tai.base.assertThat
import tai.base.getBoolean
import tai.criteria.*
import tai.criteria.ops.*
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
        val src = dialect.ctxObject[src_] as String?;
        return dialect.column(param, src);
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
        val src = dialect.ctxObject[src_] as String?;
        return dialect.table(param, src);
    }
}

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

class SqlQueryOperator : CriteriaOperation6 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(select_, true, combineMulti = ::combineSelect),
        ParamSpecMulti(from_, true, combineMulti = ::combineFrom),
        ParamSpecSingle(where_, false, emptyOp),
        ParamSpecMulti(group_by_, false, emptyOp, combineMulti = ::combineGroupBy),
        ParamSpecSingle(having_, false, emptyOp),
        ParamSpecMulti(order_by_, false, emptyOp, combineMulti = ::combineOrderBy)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        select: CriteriaExpression,
        from: CriteriaExpression,
        where: CriteriaExpression,
        groupBy: CriteriaExpression,
        having: CriteriaExpression,
        orderBy: CriteriaExpression
    ): CriteriaExpression {
        assertThat(!select.isBlank) { "Select is empty" };
        assertThat(!from.isBlank) { "From is empty" };
        val builder = CriteriaExpressionBuilderImpl()
            .add("SELECT ").add(select).add(" FROM ").add(from);

        if (!where.isBlank) {
            builder.add(" WHERE ").add(where);
        }

        if (!groupBy.isBlank) {
            builder.add(" GROUP BY ").add(groupBy);
        }

        if (!having.isBlank) {
            builder.add(" HAVING ").add(having);
        }

        if (!orderBy.isBlank) {
            builder.add(" ORDER BY ").add(orderBy);
        }

        return builder.build();
    }
}

class JoinOperator : CriteriaOperation3 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_),
        ParamSpecSingle(arg2_),
        ParamSpecSingle(arg3_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression
    ): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add(param1).add(" JOIN ").add(param2).add(" ON ").add(param3)
            .build();
    }
}

enum class Order(val value: String) {
    ASC("ASC"), DESC("DESC")
}

class OrderByOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        val order: Order = dialect.ctxObject[order_] as Order;
        return CriteriaExpressionBuilderImpl().add(param).add(" ").add(order.value).build();
    }
}
