package tai.criteria

import tai.base.JsonMap
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass

class BooleanValueHolder : CriteriaOperationNative<Boolean> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Boolean> = Boolean::class

    override fun renderExpression(dialect: CriteriaDialect, param: Boolean): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ByteValueHolder : CriteriaOperationNative<Byte> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Byte> = Byte::class

    override fun renderExpression(dialect: CriteriaDialect, param: Byte): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ShortValueHolder : CriteriaOperationNative<Short> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Short> = Short::class

    override fun renderExpression(dialect: CriteriaDialect, param: Short): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class IntValueHolder : CriteriaOperationNative<Int> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Int> = Int::class

    override fun renderExpression(dialect: CriteriaDialect, param: Int): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LongValueHolder : CriteriaOperationNative<Long> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Long> = Long::class

    override fun renderExpression(dialect: CriteriaDialect, param: Long): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class StringValueHolder : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<String> = String::class

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class DateValueHolder : CriteriaOperationNative<Date> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Date> = Date::class

    override fun renderExpression(dialect: CriteriaDialect, param: Date): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateTimeHolder : CriteriaOperationNative<LocalDateTime> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<LocalDateTime> = LocalDateTime::class

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDateTime): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateHolder : CriteriaOperationNative<LocalDate> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<LocalDate> = LocalDate::class

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
            int_value_ to IntValueHolder()
        ),
        criteriaDialect = CriteriaDialectImpl(mutableListOf(), mapOf())
    );
    val exp = kk.convert(mapOf(
        op_ to and_,
        arg_ to listOf<JsonMap>(
            mapOf(
                op_ to int_value_,
                arg_ to 5
            ),
            mapOf(
                op_ to int_value_,
                arg_ to 5
            )
        )
    ))
    println(exp)
}