package tai.criteria

import tai.base.JsonMap
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class CriteriaDialectImpl(private val paramsList: MutableList<Any>, override val ctxObject: JsonMap) : CriteriaDialect {

    override fun toExpression(param: Boolean): CriteriaExpression {
        return addParam(param);
    }

    override fun toExpression(param: Byte): CriteriaExpression {
        return addParam(param);
    }

    override fun toExpression(param: Short): CriteriaExpression {
        return addParam(param);
    }

    override fun toExpression(param: Int): CriteriaExpression {
        return addParam(param);
    }

    override fun toExpression(param: Long): CriteriaExpression {
        return addParam(param);
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
        paramsList.add(param);
        return CriteriaExpressionBuilderImpl().add("?").build();
    }
}

fun main() {
    println(
        DateTimeFormatter.ISO_INSTANT.format(LocalDateTime.now().toInstant(ZoneOffset.UTC))
    )
}