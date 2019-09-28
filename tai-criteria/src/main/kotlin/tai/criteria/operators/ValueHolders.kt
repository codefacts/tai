package tai.criteria.operators

import tai.criteria.*
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
        return dialect.toExpression(param as Byte);
    }
}

class ShortValueHolder : CriteriaOperationNative<Short> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Short> = Short::class

    override fun renderExpression(dialect: CriteriaDialect, param: Short): CriteriaExpression {
        return dialect.toExpression(param as Short);
    }
}

class IntValueHolder : CriteriaOperationNative<Int> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Int> = Int::class

    override fun renderExpression(dialect: CriteriaDialect, param: Int): CriteriaExpression {
        return dialect.toExpression(param as Int);
    }
}

class LongValueHolder : CriteriaOperationNative<Long> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Long> = Long::class

    override fun renderExpression(dialect: CriteriaDialect, param: Long): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class FloatValueHolder : CriteriaOperationNative<Float> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Float> = Float::class

    override fun renderExpression(dialect: CriteriaDialect, param: Float): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class DoubleValueHolder : CriteriaOperationNative<Double> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Double> = Double::class

    override fun renderExpression(dialect: CriteriaDialect, param: Double): CriteriaExpression {
        return dialect.toExpression(param);
    }
}

class StringValueHolder : CriteriaOperationNative<String> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<String> = String::class

    override fun renderExpression(dialect: CriteriaDialect, param: String): CriteriaExpression {
        return dialect.toExpression(param as String);
    }
}

class DateValueHolder : CriteriaOperationNative<Date> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<Date> = Date::class

    override fun renderExpression(dialect: CriteriaDialect, param: Date): CriteriaExpression {
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

class LocalDateTimeHolder : CriteriaOperationNative<LocalDateTime> {
    override val paramSpecs: Collection<ParamSpec> = listOf(ParamSpecSingle(arg_))
    override val parameterType: KClass<LocalDateTime> = LocalDateTime::class

    override fun renderExpression(dialect: CriteriaDialect, param: LocalDateTime): CriteriaExpression {
        return dialect.toExpression(param);
    }
}
