package tai.criteria

import tai.base.JsonMap
import tai.criteria.ex.CriteriaException
import java.lang.StringBuilder
import java.util.*

typealias OperationName = String;
typealias OperationMap = Map<OperationName, CriteriaOperation>;

interface CriteriaToTextConverter {
    fun convert(jsonMap: JsonMap): String;
}

class CriteriaToTextConverterImpl(
    val operationMap: OperationMap,
    val criteriaDialect: CriteriaDialect
) : CriteriaToTextConverter {

    override fun convert(jsonMap: JsonMap): String {
        return toExpression(jsonMap).toTextRepresentation(StringBuilder());
    }

    private fun toExpression(jsonMap: Map<String, Any>): CriteriaExpression {
        val operationName = jsonMap[op__];
        val criteriaOperation = operationMap[operationName]
            ?: throw CriteriaException("No criteria operation found for op = '$operationName'");

        validateCriteriaOperation(criteriaOperation);

        return when (criteriaOperation) {
            is CriteriaOperationNative<*> -> callNative(criteriaOperation, jsonMap);
            is CriteriaOperation0 -> call0(criteriaOperation, jsonMap);
            is CriteriaOperation1 -> call1(criteriaOperation, jsonMap);
            is CriteriaOperation2 -> call2(criteriaOperation, jsonMap);
            is CriteriaOperation3 -> call3(criteriaOperation, jsonMap);
            is CriteriaOperation4 -> call4(criteriaOperation, jsonMap);
            is CriteriaOperation5 -> call5(criteriaOperation, jsonMap);
            else -> throw CriteriaException("Unsupported criteria operation: $criteriaOperation");
        }
    }

    private fun validateCriteriaOperation(criteriaOperation: CriteriaOperation) {
        when (criteriaOperation) {
            is CriteriaOperationNative<*> -> assertLength(1, criteriaOperation);
            is CriteriaOperation0 -> assertLength(0, criteriaOperation);
            is CriteriaOperation1 -> assertLength(1, criteriaOperation);
            is CriteriaOperation2 -> assertLength(2, criteriaOperation);
            is CriteriaOperation3 -> assertLength(3, criteriaOperation);
            is CriteriaOperation4 -> assertLength(4, criteriaOperation);
            is CriteriaOperation5 -> assertLength(5, criteriaOperation);
            else -> throw CriteriaException("Unsupported criteria operation: $criteriaOperation");
        }
    }

    private fun assertLength(expectedSize: Int, criteriaOperation: CriteriaOperation) {
        assert(criteriaOperation.paramSpecs.size == expectedSize) {"ParamSpecs.size should be $expectedSize for criteriaOperation: $criteriaOperation"};
    }

    private fun call5(criteriaOperation: CriteriaOperation5, jsonMap: Map<String, Any>): CriteriaExpression {
        val (arg1, arg2, arg3, arg4, arg5) = toCriteriaExpression5(jsonMap, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialect, arg1, arg2, arg3, arg4, arg5);
    }

    private fun call4(criteriaOperation: CriteriaOperation4, jsonMap: Map<String, Any>): CriteriaExpression {
        val (arg1, arg2, arg3, arg4) = toCriteriaExpression4(jsonMap, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialect, arg1, arg2, arg3, arg4);
    }

    private fun call3(criteriaOperation: CriteriaOperation3, jsonMap: Map<String, Any>): CriteriaExpression {
        val (arg1, arg2, arg3) = toCriteriaExpression3(jsonMap, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialect, arg1, arg2, arg3);
    }

    private fun call2(criteriaOperation: CriteriaOperation2, jsonMap: Map<String, Any>): CriteriaExpression {
        val (arg1, arg2) = toCriteriaExpression2(jsonMap, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialect, arg1, arg2);
    }

    private fun call1(criteriaOperation: CriteriaOperation1, jsonMap: Map<String, Any>): CriteriaExpression {
        val arg: CriteriaExpression = toCriteriaExpression1(jsonMap, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialect, arg);
    }

    private fun call0(criteriaOperation: CriteriaOperation0, jsonMap: Map<String, Any>): CriteriaExpression {
        return criteriaOperation.renderExpression(criteriaDialect);
    }

    private fun callNative(
        criteriaOperation: CriteriaOperationNative<*>,
        jsonMap: Map<String, Any>
    ): CriteriaExpression {

        val (name, isMandatory, defaultValue) = criteriaOperation.paramSpecs.iterator().next();
        val operatorName = jsonMap[op__] as String;

        val arg = if (isMandatory) {
            Objects.requireNonNull(
                jsonMap[name], valueRequiredMsg(name, operatorName)
            )
        } else {
            jsonMap.getOrDefault(name, Objects.requireNonNull(
                defaultValue, noDefaultValueGivenMsg(name, operatorName)
            ));
        }

        assert(arg!!::class == criteriaOperation.parameterType) {invalidArgumentTypeMsg(arg, name, operatorName)}

        return (criteriaOperation as CriteriaOperationNative<Any>).renderExpression(criteriaDialect, arg);
    }

    private fun invalidArgumentTypeMsg(arg: Any, name: String, operatorName: String): String {
        return "Argument '$arg' with invalid type '${arg::class}' provided for parameter '$name' in operator '$operatorName'";
    }

    private fun toCriteriaExpression5(jsonMap: Map<String, Any>, paramSpecs: Collection<ParamSpec>): Exp5 {
        val arg1 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg2 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg3 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg4 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg5 = argToExpression(jsonMap, paramSpecs.iterator().next());
        return Exp5(arg1, arg2, arg3, arg4, arg5);
    }

    private fun toCriteriaExpression4(jsonMap: Map<String, Any>, paramSpecs: Collection<ParamSpec>): Exp4 {
        val arg1 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg2 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg3 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg4 = argToExpression(jsonMap, paramSpecs.iterator().next());
        return Exp4(arg1, arg2, arg3, arg4);
    }

    private fun toCriteriaExpression3(jsonMap: Map<String, Any>, paramSpecs: Collection<ParamSpec>): Exp3 {
        val arg1 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg2 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg3 = argToExpression(jsonMap, paramSpecs.iterator().next());
        return Exp3(arg1, arg2, arg3);
    }

    private fun toCriteriaExpression2(jsonMap: Map<String, Any>, paramSpecs: Collection<ParamSpec>): Exp2 {
        val arg1 = argToExpression(jsonMap, paramSpecs.iterator().next());
        val arg2 = argToExpression(jsonMap, paramSpecs.iterator().next());
        return Exp2(arg1, arg2);
    }

    private fun toCriteriaExpression1(
        jsonMap: Map<String, Any>,
        paramSpecs: Collection<ParamSpec>
    ): CriteriaExpression {
        return argToExpression(jsonMap, paramSpecs.iterator().next());
    }

    private fun argToExpression(jsonMap: JsonMap, paramSpec: ParamSpec): CriteriaExpression {
        return when (paramSpec) {
            is ParamSpecSingle -> argToExpSingle(jsonMap, paramSpec);
            is ParamSpecMulti -> argToExpMulti(jsonMap, paramSpec);
        }
    }

    private fun argToExpSingle(jsonMap: JsonMap, paramSpec: ParamSpecSingle): CriteriaExpression {
        val opJsonMap = getOpJsonForKey(jsonMap, paramSpec);
        if (opJsonMap !is Map<*, *>) {
            throwInvalidArgumentTypeEx(paramSpec.name, "Map", opJsonMap::class.java.simpleName, jsonMap);
        }
        return toExpression(opJsonMap as JsonMap);
    }

    private fun argToExpMulti(jsonMap: JsonMap, paramSpec: ParamSpecMulti): CriteriaExpression {
        val (name) = paramSpec;
        val opJsonList = getOpJsonForKey(jsonMap, paramSpec);
        if (opJsonList !is List<*>) {
            throwInvalidArgumentTypeEx(name, "List", opJsonList::class.java.simpleName, jsonMap);
        }
        return paramSpec.combineMulti(criteriaDialect, opJsonList.map { toExpression(it as JsonMap) });
    }

    private fun getOpJsonForKey(jsonMap: JsonMap, paramSpec: ParamSpec): Any {
        val (name, isMandatory, defaultValue) = paramSpec;
        val operatorName = jsonMap[op__] as String;
        val opJsonMap = if (isMandatory) {
            jsonMap[name]
        } else {
            jsonMap.getOrDefault(name, Objects.requireNonNull(
                defaultValue, noDefaultValueGivenMsg(name, operatorName)
            ));
        }
        return Objects.requireNonNull(opJsonMap, valueRequiredMsg(name, operatorName))!!;
    }

    private fun valueRequiredMsg(name: String, operatorName: String): String {
        return "No value given for parameter '$name' in operator '$operatorName'";
    }

    private fun noDefaultValueGivenMsg(name: String, operatorName: String): String {
        return "No default value given for parameter '$name' in operator '$operatorName'";
    }

    private fun throwInvalidArgumentTypeEx(name: String, expectedType: String, actualType: String, jsonMap: Map<String, Any>): Nothing {
        throw CriteriaException("Invalid argument type for key '$name' in jsonMap = $jsonMap, expected type = $expectedType but actual type = $actualType");
    }
}

data class Exp2(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression
);

data class Exp3(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression
);

data class Exp4(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression,
    val arg4: CriteriaExpression
);

data class Exp5(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression,
    val arg4: CriteriaExpression,
    val arg5: CriteriaExpression
);