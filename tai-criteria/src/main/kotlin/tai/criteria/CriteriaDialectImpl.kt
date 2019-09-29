package tai.criteria

import tai.base.JsonMap
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KClass

class CriteriaDialectImpl(private val paramsBuilder: ParamsBuilder) : CriteriaDialect {

    override fun toExpression(param: Float): CriteriaExpression {
        assertType(param, Float::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun toExpression(param: Double): CriteriaExpression {
        assertType(param, Double::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun column(column: String, src: String?): CriteriaExpression {
        return if (src.isNullOrEmpty()) quote(column)
        else CriteriaExpressionBuilderImpl().add(src).add(".").add(quote(column)).build();
    }

    override fun table(table: String, src: String?): CriteriaExpression {
        return if (src.isNullOrEmpty()) quote(table)
        else CriteriaExpressionBuilderImpl().add(src).add(".").add(quote(table)).build();
    }

    override fun quote(name: String): CriteriaExpression {
        return CriteriaExpressionBuilderImpl().add("`$name`").build();
    }

    override val ctxObject: JsonMap
        get() = throw RuntimeException("This instance of criteria dialect (${this::class.simpleName}) does not support ctxObject");

    override fun toExpression(param: Boolean): CriteriaExpression {
        assertType(param, Boolean::class);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
    }

    override fun toExpression(param: Byte): CriteriaExpression {
        assertType(param, Byte::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun toExpression(param: Short): CriteriaExpression {
        assertType(param, Short::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun toExpression(param: Int): CriteriaExpression {
        assertType(param, Int::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun toExpression(param: Long): CriteriaExpression {
        assertType(param, Long::class);
        return CriteriaExpressionBuilderImpl().add(
            if (param < 0) "($param)" else param.toString()
        ).build();
    }

    override fun toExpression(param: String): CriteriaExpression {
        return addParam(param);
    }

    override fun toExpression(param: Date): CriteriaExpression {
        return addParam(DateTimeFormatter.ISO_INSTANT.format(param.toInstant()));
    }

    override fun toExpression(param: LocalDate): CriteriaExpression {
        return addParam(
            DateTimeFormatter.ISO_INSTANT.format(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC))
        );
    }

    override fun toExpression(param: LocalDateTime): CriteriaExpression {
        return addParam(
            DateTimeFormatter.ISO_INSTANT.format(param.toInstant(ZoneOffset.UTC))
        );
    }

    private fun addParam(param: Any): CriteriaExpression {
        paramsBuilder.add(param);
        return CriteriaExpressionBuilderImpl().add("?").build();
    }

    private fun <T : Any> assertType(param: T, clazz: KClass<T>) {
        val prm = param as Any;
        assert(prm::class == clazz) {
            "Invalid param, expected type = ${clazz.simpleName} | actual type = ${prm::class.simpleName}"
        }
    }
}

interface CriteriaDialectBuilder {
    fun build(paramsBuilder: ParamsBuilder): CriteriaDialect;
}

class CriteriaDialectBuilderImpl : CriteriaDialectBuilder {
    override fun build(paramsBuilder: ParamsBuilder): CriteriaDialect {
        return CriteriaDialectImpl(paramsBuilder);
    }
}

fun main() {
    println(
        DateTimeFormatter.ISO_INSTANT.format(LocalDateTime.now().toInstant(ZoneOffset.UTC))
    )
}