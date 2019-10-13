package tai.criteria.operators

import tai.base.JsonMap
import tai.base.assertThat
import tai.criteria.*
import tai.criteria.ex.CriteriaException
import tai.criteria.ops.*
import kotlin.reflect.KClass

class PathExpressionOperator : CriteriaOperation0 {
    override val paramSpecs: Collection<ParamSpec> = listOf();

    override fun renderExpression(dialect: CriteriaDialect): CriteriaExpression {
        val arg = dialect.ctxObject[arg_] as List<String>?
            ?: throw CriteriaException("No arg provided to ${this::class.simpleName}");
        if (arg.isEmpty()) {
            throw CriteriaException("Empty arg list provided to ${this::class.simpleName}");
        }
        return joinCriteriaExpressions(
            arg.map { CritExpSingle(it) }, "."
        )
    }
}

class SelectOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineSelect)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        assertThat(!param.isBlank) { "Mandatory Select is empty" };
        return CriteriaExpressionBuilderImpl()
            .add("SELECT ").add(param).build();
    }
}

class FromOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineFrom)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        assertThat(!param.isBlank) { "Mandatory From is empty" };
        return CriteriaExpressionBuilderImpl()
            .add("FROM ").add(param).build();
    }
}

class JoinOperator : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_),
        ParamSpecSingle(arg2_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        joiningTable: CriteriaExpression,
        on: CriteriaExpression
    ): CriteriaExpression {

        val joinStr = when (val joinType = dialect.ctxObject[join_type_]) {
            null -> "JOIN"
            is JoinType -> joinType.value
            is CharSequence -> joinType.toString();
            else -> throw CriteriaException("Invalid join type option [${joinType::class.java.simpleName}] provided in JoinOperator")
        };

        return CriteriaExpressionBuilderImpl()
            .add(joinStr).add(" ").add(joiningTable).add(" ON ").add(on).build();
    }
}

class WhereOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_, false, emptyCriteriaOp)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) {
            return emptyCriteriaExpression;
        }
        return CriteriaExpressionBuilderImpl()
            .add("WHERE ").add(param).build();
    }
}

class GroupByOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, false, emptyCriteriaOp, combineMulti = ::combineGroupBy)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) {
            return emptyCriteriaExpression;
        }
        return CriteriaExpressionBuilderImpl().add("GROUP BY ").add(param).build();
    }
}

class HavingOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_, false, emptyCriteriaOp)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) return emptyCriteriaExpression;
        return CriteriaExpressionBuilderImpl().add("HAVING ").add(param).build();
    }
}

class OrderByOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, false, emptyCriteriaOp, combineMulti = ::combineOrderBy)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        if (param.isBlank) return emptyCriteriaExpression;
        return CriteriaExpressionBuilderImpl().add("ORDER BY ").add(param).build();
    }
}

class OrderOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        val order = dialect.ctxObject[arg2_] as Order?;
        return CriteriaExpressionBuilderImpl().add(param).add(" ").add(order?.value ?: Order.ASC.value).build();
    }
}

class JoinExpressionsOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineExpressions)
    );

    private fun combineExpressions(
        criteriaDialect: CriteriaDialect,
        list: List<CriteriaExpression>
    ): CriteriaExpression {
        val delimiter = criteriaDialect.ctxObject[delimiter_] as String? ?: " ";
        return joinCriteriaExpressions(list, delimiter);
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return param;
    }
}

class SqlSelectIntoOperator : CriteriaOperation3 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(select_, true, combineMulti = ::combineSelect),
        ParamSpecSingle(into_, true),
        ParamSpecSingle(in_, false, emptyCriteriaOp)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        select: CriteriaExpression,
        into: CriteriaExpression,
        inExp: CriteriaExpression
    ): CriteriaExpression {
        assertThat(!select.isBlank) { "Mandatory Select is empty" };
        assertThat(!into.isBlank) { "Mandatory Into is empty" };

        val builder = CriteriaExpressionBuilderImpl()
            .add("SELECT ").add(select).add(" INTO ").add(into);
        if (!inExp.isBlank) {
            builder.add(" IN ").add(inExp);
        }
        return builder.build();
    }
}

class SqlInsertIntoOperator : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(table_),
        ParamSpecMulti(columns_, false, defaultValue = listOf<JsonMap>(), combineMulti = ::combineColumns)
    );

    private fun combineColumns(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, ", ");
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        table: CriteriaExpression,
        columns: CriteriaExpression
    ): CriteriaExpression {
        val builder = CriteriaExpressionBuilderImpl()
            .add("INSERT INTO ").add(table);
        if (!columns.isBlank) {
            builder.add(" (").add(columns).add(")");
        }
        return builder.build();
    }
}

class InsertOperator : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(table_),
        ParamSpecMulti(columns_, combineMulti = ::combineColumns)
    );

    private fun combineColumns(dialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, ", ")
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        table: CriteriaExpression,
        columns: CriteriaExpression
    ): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("INSERT INTO ").add(table).add(" (").add(columns).add(")").build();
    }
}

class UpdateOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(table_)
    );

    override fun renderExpression(dialect: CriteriaDialect, table: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("UPDATE ").add(table).build();
    }
}

class DeleteOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    );

    override fun renderExpression(dialect: CriteriaDialect, table: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("DELETE FROM ").add(table).build();
    }
}

class SetOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineMulti)
    )

    private fun combineMulti(
        dialect: CriteriaDialect,
        list: List<CriteriaExpression>
    ): CriteriaExpression {
        return joinCriteriaExpressions(list, ", ");
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("SET ").add(param).build();
    }
}

class SqlValuesOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineValues)
    );

    private fun combineValues(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(
            list, ", "
        );
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("VALUES (").add(param).add(")").build();
    }
}

class LimitOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    )

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("LIMIT ").add(param).build()
    }
}

class OffsetOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    )

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("OFFSET ").add(param).build()
    }
}

class UnionOperator : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_),
        ParamSpecSingle(arg2_)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression
    ): CriteriaExpression {
        val isAll = dialect.ctxObject[is_all_] as Boolean?;
        if (isAll != null && isAll) {
            return CriteriaExpressionBuilderImpl()
                .add(param1).add(" UNION ALL ").add(param2).build();
        }
        return CriteriaExpressionBuilderImpl()
            .add(param1).add(" UNION ").add(param2).build();
    }
}

class ExistsOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg_)
    );

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        val isNot = dialect.ctxObject[is_not_] as Boolean?;
        if (isNot != null && isNot) {
            return CriteriaExpressionBuilderImpl()
                .add("NOT EXISTS ").add(param).build();
        }
        return CriteriaExpressionBuilderImpl()
            .add("EXISTS ").add(param).build();
    }
}
