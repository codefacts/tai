package tai.criteria.operators

import tai.base.JsonMap
import tai.base.assertThat
import tai.criteria.*
import tai.criteria.ex.CriteriaException
import tai.criteria.ops.*
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

        val joinStr = when (val joinType = dialect.ctxObject[join_type_]) {
            null -> "JOIN"
            is JoinType -> joinType.value
            is CharSequence -> joinType.toString();
            else -> throw CriteriaException("Invalid join type option [${joinType::class.java.simpleName}] provided in JoinOperator")
        };

        return CriteriaExpressionBuilderImpl()
            .add(param1).add(" ").add(joinStr).add(" ").add(param2).add(" ON ").add(param3)
            .build();
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

class CriteriaJoinOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineExpressions)
    );

    private fun combineExpressions(
        criteriaDialect: CriteriaDialect,
        list: List<CriteriaExpression>
    ): CriteriaExpression {
        val delimiter = criteriaDialect.ctxObject[delimiter_] as String?;
        return joinCriteriaExpressions(list, delimiter ?: " ");
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

class SqlInsertIntoOperator : CriteriaOperation3 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(table_),
        ParamSpecMulti(columns_, false, defaultValue = listOf<JsonMap>(), combineMulti = ::combineColumns),
        ParamSpecSingle(expression_)
    );

    private fun combineColumns(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        if (list.isEmpty()) return emptyCriteriaExpression;
        return CriteriaExpressionBuilderImpl()
            .add("(").add(joinCriteriaExpressions(list, ", ")).add(")").build();
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        table: CriteriaExpression,
        columns: CriteriaExpression,
        expression: CriteriaExpression
    ): CriteriaExpression {
        val builder = CriteriaExpressionBuilderImpl()
            .add("INSERT INTO ").add(table);
        if (!columns.isBlank) {
            builder.add(" ").add(columns)
        }
        return builder.add(" ").add(expression).build();
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

class DeleteOperator : CriteriaOperation0 {
    override val paramSpecs: Collection<ParamSpec> = listOf();

    override fun renderExpression(dialect: CriteriaDialect): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("DELETE ").build();
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
            list.map { exp -> CriteriaExpressionBuilderImpl().add("VALUES ").add(exp).build() }, ", "
        );
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return param;
    }
}

class SqlValueArrayOperator : CriteriaOperation1 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(arg_, true, combineMulti = ::combineValues)
    );

    private fun combineValues(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        if (list.isEmpty()) return emptyCriteriaExpression;
        return CriteriaExpressionBuilderImpl()
            .add("(").add(joinCriteriaExpressions(list, ", ")).add(")").build();
    }

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return param;
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