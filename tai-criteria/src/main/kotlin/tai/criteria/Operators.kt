package tai.criteria

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass

const val op__ = "op";
const val arg__ = "arg";
const val arg1__ = "arg1";
const val arg2__ = "arg2";
const val arg3__ = "arg3";
const val arg4__ = "arg4";
const val arg5__ = "arg5";

class BooleanValueHolder : CriteriaOperationNative<Boolean> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Boolean> = Boolean::class

    override fun renderExpression(dialect: CriteriaDialect, param: Boolean): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ByteValueHolder : CriteriaOperationNative<Byte> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Byte> = Byte::class

    override fun renderExpression(dialect: CriteriaDialect, param: Byte): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class ShortValueHolder : CriteriaOperationNative<Short> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Short> = Short::class

    override fun renderExpression(dialect: CriteriaDialect, param: Short): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class IntValueHolder : CriteriaOperationNative<Int> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Int> = Int::class

    override fun renderExpression(dialect: CriteriaDialect, param: Int): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LongValueHolder : CriteriaOperationNative<Long> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Long> = Long::class

    override fun renderExpression(dialect: CriteriaDialect, param: Long): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class StringValueHolder : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<String> = String::class

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class DateValueHolder : CriteriaOperationNative<Date> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<Date> = Date::class

    override fun renderExpression(dialect: CriteriaDialect, param: Date): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateTimeHolder : CriteriaOperationNative<LocalDateTime> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<LocalDateTime> = LocalDateTime::class

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDateTime): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class LocalDateHolder : CriteriaOperationNative<LocalDate> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg__))
    override val parameterType: KClass<LocalDate> = LocalDate::class

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDate): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class AndOperator : CriteriaOperation1 {

    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecMulti(
        arg__, true, null, ::combineMulti
    ))

    private fun combineMulti(criteriaDialect: CriteriaDialect, list: List<CriteriaExpression>): CriteriaExpression {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}