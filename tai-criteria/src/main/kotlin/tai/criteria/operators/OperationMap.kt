package tai.criteria.operators

import tai.criteria.*
import java.util.*

typealias OperationName = String;

interface OperationMap {
    val operationMap: Map<OperationName, CriteriaOperation>;
    operator fun get(key: String): CriteriaOperation;
};

class OperationMapImpl(override val operationMap: Map<String, CriteriaOperation>): OperationMap {
    override fun get(key: String): CriteriaOperation {
        return Objects.requireNonNull(
            operationMap[key], "No criteria operation found for operation name '$key'"
        )!!;
    }
}

val operationMap = OperationMapImpl(mapOf(
    boolean_value_ to BooleanValueHolder(),
    byte_value_ to ByteValueHolder(),
    short_value_ to ShortValueHolder(),
    int_value_ to IntValueHolder(),
    long_value_ to LongValueHolder(),
    string_value_ to StringValueHolder(),
    date_value_ to DateValueHolder(),
    local_date_value_ to LocalDateHolder(),
    local_date_time_value_ to LocalDateTimeHolder(),
    //SqlSyntax
    column_ to ColumnNameOperator(),
    table_ to TableNameOperator(),
    as_ to AsOperator(),
    //logical operators
    and_ to AndOperatorImpl(),
    or_ to OrOperatorImpl(),
    not_ to NotOperatorImpl(),
    in_ to InOperatorImpl(),
    between_ to BetweenOperatorImpl(),
    like_ to LikeOperatorImpl(),
    //comparision operator
    eq_ to createGenericBiOperator(" = "),
    neq_ to createGenericBiOperator(" <> "),
    gt_ to createGenericBiOperator(" > "),
    gte_ to createGenericBiOperator(" >= "),
    lt_ to createGenericBiOperator(" < "),
    lte_ to createGenericBiOperator(" <= ")
));