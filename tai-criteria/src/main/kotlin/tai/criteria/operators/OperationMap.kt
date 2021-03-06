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
            operationMap[key], "No criteria operation found in the operation map for operation name '$key'"
        )!!;
    }
}

val OPERATION_MAP = OperationMapImpl(
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
        local_time_value_ to LocalTimeValueHolder(),
        instant_ to InstantValueHolder(),
        local_date_value_ to LocalDateHolder(),
        local_date_time_value_ to LocalDateTimeHolder(),
        empty_ to EmptyCriteriaOperator(),
        null_value_ to NullValueHolder(),

        //SqlOperators
        path_expression_ to PathExpressionOperator(),
        as_ to AsOperator(),
        join_ to JoinOperator(),
        order_by_ to OrderByOperator(),
        order_ to OrderOperator(),
        union_ to UnionOperator(),
        exists_ to ExistsOperator(),
        insert_into_ to SqlInsertIntoOperator(),
        select_into_ to SqlSelectIntoOperator(),
        sql_values_ to SqlValuesOperator(),

        select_ to SelectOperator(),
        from_ to FromOperator(),
        where_ to WhereOperator(),
        group_by_ to GroupByOperator(),
        having_ to HavingOperator(),
        join_expressions_ to JoinExpressionsOperator(),
        insert_ to InsertOperator(),
        update_ to UpdateOperator(),
        delete_ to DeleteOperator(),
        set_ to SetOperator(),

        //logical operators
        and_ to AndOperatorImpl(),
        or_ to OrOperatorImpl(),
        not_ to NotOperatorImpl(),
        in_ to InOperatorImpl(),
        between_ to BetweenOperatorImpl(),
        like_ to LikeOperatorImpl(),
        is_ to IsOperator(),
        limit_ to LimitOperator(),
        offset_ to OffsetOperator(),

        //comparision operator
        eq_ to createComparisionOperator(" = "),
        neq_ to createComparisionOperator(" <> "),
        gt_ to createComparisionOperator(" > "),
        gte_ to createComparisionOperator(" >= "),
        lt_ to createComparisionOperator(" < "),
        lte_ to createComparisionOperator(" <= "),

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
        count_ to createOneArgFun("COUNT"),
        distinct_ to DistinctOperator(),

        //Others
        star_ to StartOperator()
    )
);