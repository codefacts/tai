package tai.criteria.operators

import tai.criteria.*
import tai.criteria.ops.*
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
        if (param.isBlank) return param;
        return withParenthesis(param);
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
        return withParenthesis(param);
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
            .add("NOT ")
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
        val isNot = dialect.ctxObject[is_not_] as Boolean?;
        if (param2.isBlank) {
            return dialect.toExpression(isNot != null && isNot);
        }
        return withParenthesis(
            if (isNot != null && isNot) {
                CriteriaExpressionBuilderImpl().add(param1).add(" NOT IN ").add(param2).build()
            } else {
                CriteriaExpressionBuilderImpl().add(param1).add(" IN ").add(param2).build();
            }
        )
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
        val isNot = dialect.ctxObject[is_not_] as Boolean?;
        return withParenthesis(
            if (isNot != null && isNot) {
                CriteriaExpressionBuilderImpl().add(param1).add(" NOT BETWEEN ").add(param2).add(" AND ")
                    .add(param3)
                    .build()
            } else {
                CriteriaExpressionBuilderImpl().add(param1).add(" BETWEEN ").add(param2).add(" AND ").add(param3)
                    .build()
            }
        )
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
        val isNot = dialect.ctxObject[is_not_] as Boolean?;
        return withParenthesis(
            if (isNot != null && isNot) {
                CriteriaExpressionBuilderImpl().add(param1).add(" NOT LIKE ").add(param2).build()
            } else {
                CriteriaExpressionBuilderImpl().add(param1).add(" LIKE ").add(param2).build()
            }
        )
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
                like(
                    column("email"),
                    valueOf("soha@kk")
                ),
                gt(
                    asOp(
                        column("age", "u"),
                        "user_age"
                    ),
                    valueOf(15)
                )
            ),
            and(
                valueOf(Date()),
                valueOf("Amari"),
                valueOf(true),
                valueOf(12),
                plus(
                    valueOf(13),
                    valueOf(4),
                    valueOf(-8),
                    divide(
                        valueOf(9),
                        valueOf(3)
                    ),
                    plus(
                        valueOf(45.125),
                        valueOf(-9.45),
                        minus(
                            valueOf(78),
                            valueOf(-57F)
                        )
                    )
                )
            )
        )
    )
    println(exp)
}