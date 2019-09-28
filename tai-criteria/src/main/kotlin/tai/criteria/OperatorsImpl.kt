package tai.criteria

import tai.base.JsonMap
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class BooleanValueHolder : CriteriaOperationNative<Boolean> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Boolean> = Boolean::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Boolean): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ByteValueHolder : CriteriaOperationNative<Byte> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Byte> = Byte::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Byte): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ShortValueHolder : CriteriaOperationNative<Short> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Short> = Short::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Short): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class IntValueHolder : CriteriaOperationNative<Int> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Int> = Int::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Int): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LongValueHolder : CriteriaOperationNative<Long> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Long> = Long::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Long): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class StringValueHolder : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<String> = String::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class DateValueHolder : CriteriaOperationNative<Date> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<Date> = Date::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: Date): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateTimeHolder : CriteriaOperationNative<LocalDateTime> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<LocalDateTime> = LocalDateTime::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDateTime): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateHolder : CriteriaOperationNative<LocalDate> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: Class<LocalDate> = LocalDate::class.java

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDate): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class AndOperatorImpl : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(
        ParamSpecMulti(
            arg_, true, null, ::combineMulti
        )
    )

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        val expBuilder = CriteriaExpressionBuilderImpl();
        if (list.isEmpty()) {
            return criteriaDialect.toExpression(true);
        }
        expBuilder.add("( ");
        for (i in 0..(list.size - 2)) {
            expBuilder.add(list[i]).add(" AND ");
        }
        return expBuilder.add(list[list.size - 1]).add(" )").build();
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        return param;
    }
}

fun main() {
    val kk = CriteriaToTextConverterImpl(
        operationMap = mapOf(
            and_ to AndOperatorImpl(),
            int_value_ to IntValueHolder(),
            string_value_ to StringValueHolder(),
            date_value_ to DateValueHolder(),
            local_date_value_ to LocalDateHolder(),
            local_date_time_value_ to LocalDateTimeHolder()
        ),
        criteriaDialectBuilder = CriteriaDialectBuilderImpl()
    );
    val exp = kk.convert(mapOf(
        op_ to and_,
        arg_ to listOf<JsonMap>(
            mapOf(
                op_ to and_,
                arg_ to listOf<JsonMap>(
                    mapOf(
                        op_ to date_value_,
                        arg_ to Date()
                    ),
                    mapOf(
                        op_ to string_value_,
                        arg_ to "Amari Sona"
                    ),
                    mapOf(
                        op_ to local_date_value_,
                        arg_ to LocalDate.now()
                    ),
                    mapOf(
                        op_ to int_value_,
                        arg_ to 5
                    )
                )
            ),
            mapOf(
                op_ to and_,
                arg_ to listOf<JsonMap>(
                    mapOf(
                        op_ to int_value_,
                        arg_ to 5
                    ),
                    mapOf(
                        op_ to int_value_,
                        arg_ to 5
                    ),
                    mapOf(
                        op_ to local_date_time_value_,
                        arg_ to LocalDateTime.now()
                    ),
                    mapOf(
                        op_ to int_value_,
                        arg_ to 5
                    )
                )
            )
        )
    ))
    println(exp)
}