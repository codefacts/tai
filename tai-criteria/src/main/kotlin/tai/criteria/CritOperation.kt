package tai.criteria

import tai.base.JsonMap
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass

sealed class ParamSpec(
    open val name: String,
    open val isMandatory: Boolean = true,
    open val defaultValue: Any? = null
) {
    open operator fun component1() = name;
    open operator fun component2() = isMandatory;
    open operator fun component3() = defaultValue;
}

data class ParamSpecSingle(
    override val name: String,
    override val isMandatory: Boolean = true,
    override val defaultValue: Any? = null
) : ParamSpec(name, isMandatory, defaultValue);

data class ParamSpecMulti(
    override val name: String,
    override val isMandatory: Boolean = true,
    override val defaultValue: Any? = null,
    val combineMulti: (CriteriaDialect, List<CriteriaExpression>) -> CriteriaExpression
) : ParamSpec(name, isMandatory, defaultValue);

interface CriteriaDialect {
    val ctxObject: JsonMap;
    fun toExpression(param: Boolean): CriteriaExpression;
    fun toExpression(param: Byte): CriteriaExpression;
    fun toExpression(param: Short): CriteriaExpression;
    fun toExpression(param: Int): CriteriaExpression;
    fun toExpression(param: Long): CriteriaExpression;
    fun toExpression(param: Float): CriteriaExpression;
    fun toExpression(param: Double): CriteriaExpression;
    fun toExpression(param: String): CriteriaExpression;
    fun toExpression(param: Date): CriteriaExpression;
    fun toExpression(param: LocalDate): CriteriaExpression;
    fun toExpression(param: LocalDateTime): CriteriaExpression;
    fun column(column: String, src: String? = null): CriteriaExpression;
    fun table(table: String, src: String? = null): CriteriaExpression;
    fun quote(name: String): CriteriaExpression;
}

interface CriteriaExpression {
    val isBlank: Boolean;
    fun toTextRepresentation(): String;
    fun toInternalRepresentation(stringBuilder: StringBuilder): StringBuilder;
}

interface CriteriaExpressionBuilder {
    fun add(text: String): CriteriaExpressionBuilder;
    fun add(criteriaExpression: CriteriaExpression): CriteriaExpressionBuilder;
    fun build(): CriteriaExpression;
}

interface CriteriaOperation {
    val paramSpecs: Collection<ParamSpec>;
}

interface CriteriaOperationNative<T : Any> : CriteriaOperation {
    val parameterType: KClass<T>;
    fun renderExpression(
        dialect: CriteriaDialect,
        param: T
    ): CriteriaExpression
}

interface CriteriaOperation0 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect
    ): CriteriaExpression
}

interface CriteriaOperation1 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation2 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation3 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation4 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression,
        param4: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation5 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression,
        param4: CriteriaExpression,
        param5: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation6 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression,
        param4: CriteriaExpression,
        param5: CriteriaExpression,
        param6: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation7 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression,
        param4: CriteriaExpression,
        param5: CriteriaExpression,
        param6: CriteriaExpression,
        param7: CriteriaExpression
    ): CriteriaExpression
}

interface CriteriaOperation8 : CriteriaOperation {
    fun renderExpression(
        dialect: CriteriaDialect,
        param1: CriteriaExpression,
        param2: CriteriaExpression,
        param3: CriteriaExpression,
        param4: CriteriaExpression,
        param5: CriteriaExpression,
        param6: CriteriaExpression,
        param7: CriteriaExpression,
        param8: CriteriaExpression
    ): CriteriaExpression
}