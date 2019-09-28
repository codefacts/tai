package tai.criteria.operators

import tai.criteria.*
import java.util.*

class AndOperatorImpl : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(
            arg_, true, null, ::combineMulti
        )
    )

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " AND ");
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        if (param.isEmpty) return dialect.toExpression(true);
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
            expBuilder -> expBuilder.add(param)
        }.build();
    }
}

class OrOperatorImpl : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(
            arg_, true, null, ::combineMulti
        )
    )

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, " OR ");
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        if (param.isEmpty) return dialect.toExpression(true);
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
            expBuilder -> expBuilder.add(param);
        }.build();
    }
}

class NotOperatorImpl : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(
            arg_, true
        )
    )

    override fun renderExpression(dialect: CriteriaDialect, param: CriteriaExpression): CriteriaExpression {
        return CriteriaExpressionBuilderImpl()
            .add("!")
            .add(param)
            .build();
    }
}

class InOperatorImpl : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_, true),
        ParamSpecMulti(arg2_, isMandatory = true, combineMulti = ::combineMulti)
    );

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        return joinCriteriaExpressions(list, ", ");
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression
    ): CriteriaExpression {
        if (param2.isEmpty) {
            return dialect.toExpression(false);
        }
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
                expBuilder -> expBuilder.add(param1).add(" IN ").add(param2)
        }.build();
    }
}

class BetweenOperatorImpl : CriteriaOperation3 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_, true),
        ParamSpecSingle(arg2_, true),
        ParamSpecSingle(arg3_, true)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression
    ): CriteriaExpression {
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
            expBuilder -> expBuilder.add(param1).add(" BETWEEN ").add(param2).add(" AND ").add(param3);
        }.build();
    }
}

class LikeOperatorImpl : CriteriaOperation2 {
    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecSingle(arg1_, true),
        ParamSpecSingle(arg2_, true)
    );

    override fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression
    ): CriteriaExpression {
        return withParenthesis(CriteriaExpressionBuilderImpl()) {
                expBuilder -> expBuilder.add(param1).add(" LIKE ").add(param2);
        }.build();
    }
}

fun main() {
    val kk = CriteriaToTextConverterImpl(
        operationMap,
        criteriaDialectBuilder = CriteriaDialectBuilderImpl()
    );
    val exp = kk.convert(
        and(
            and(
                eq(
                    column("name", "u"),
                    valueOf("Amari")
                ),
                eq(
                    column("email"),
                    valueOf("soha@kk")
                ),
                eq(
                    asOp(
                        column("age", "u"),
                        "user_age"
                    ),
                    valueOf(true)
                )
            ),
            and(
                valueOf(Date()),
                valueOf("Amari"),
                valueOf(true),
                valueOf(12)
            )
        )
    )
    println(exp)
}