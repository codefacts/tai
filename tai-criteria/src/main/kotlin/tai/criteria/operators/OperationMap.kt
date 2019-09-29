package tai.criteria.operators

import tai.criteria.*
import java.util.*

typealias OperationName = String;

interface OperationMap {
    val operationMap: Map<OperationName, CriteriaOperation>;
    operator fun get(key: String): CriteriaOperation;
};

class OperationMapImpl(override val operationMap: Map<String, CriteriaOperation>) : OperationMap {
    override fun get(key: String): CriteriaOperation {
        return Objects.requireNonNull(
            operationMap[key], "No criteria operation found for operation name '$key' in the operation map"
        )!!;
    }
}

val operationMap = OperationMapImpl(
    mapOf(
        boolean_value_ to BooleanValueHolder(),
        byte_value_ to ByteValueHolder(),
        short_value_ to ShortValueHolder(),
        int_value_ to IntValueHolder(),
        long_value_ to LongValueHolder(),
        float_value_ to FloatValueHolder(),
        double_value_ to DoubleValueHolder(),
        string_value_ to StringValueHolder(),
        date_value_ to DateValueHolder(),
        local_date_value_ to LocalDateHolder(),
        local_date_time_value_ to LocalDateTimeHolder(),
        empty_ to EmptyValueHolder(),
        //SqlOperators
        sql_query_ to SqlQueryOperator(),
        column_ to ColumnNameOperator(),
        table_ to TableNameOperator(),
        as_ to AsOperator(),
        join_ to JoinOperator(),
        order_by_ to OrderByOperator(),
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
        lte_ to createGenericBiOperator(" <= "),
        //Arithmetic operator
        plus_ to PlusOperator(),
        minus_ to createGenericBiOperator(" - "),
        multiply_ to MultiplyOperator(),
        divide_ to createGenericBiOperator(" / "),
        modulo_ to createGenericBiOperator(" % "),
        //Aggregate
        avg_ to createOneArgFun("AVG"),
        sum_ to createOneArgFun("SUM"),
        min_ to createOneArgFun("MIN"),
        max_ to createOneArgFun("MAX"),
        count_ to createOneArgFun("COUNT")
    )
);