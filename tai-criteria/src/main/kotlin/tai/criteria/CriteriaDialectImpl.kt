package tai.criteria

import tai.base.JsonMap
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class CriteriaDialectImpl(private val paramsBuilder: ParamsBuilder) : CriteriaDialect {

    override fun column(column: String): CriteriaExpression {
        return quote(column);
    }

    override fun table(table: String): CriteriaExpression {
        return quote(table);
    }

    override fun quote(name: String): CriteriaExpression {
        return CriteriaExpressionBuilderImpl().add("`$name`").build();
    }

    override val ctxObject: JsonMap
        get() = throw RuntimeException("This instance of criteria dialect (${this::class.java.simpleName}) does not support ctxObject");

    override fun toExpression(param: Boolean): CriteriaExpression {
        assertType(param, Boolean::class.java);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
    }

    override fun toExpression(param: Byte): CriteriaExpression {
        assertType(param, Byte::class.java);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
    }

    override fun toExpression(param: Short): CriteriaExpression {
        assertType(param, Short::class.java);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
    }

    override fun toExpression(param: Int): CriteriaExpression {
        assertType(param, Int::class.java);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
    }

    override fun toExpression(param: Long): CriteriaExpression {
        assertType(param, Long::class.java);
        return CriteriaExpressionBuilderImpl().add(param.toString()).build();
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

    private fun <T> assertType(param: T, clazz: Class<T>) {
        val prm = param as Any;
        assert(prm::class.java === clazz) {
            "Invalid param, expected type = ${clazz.simpleName} | actual type = ${prm::class.java.simpleName}"
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