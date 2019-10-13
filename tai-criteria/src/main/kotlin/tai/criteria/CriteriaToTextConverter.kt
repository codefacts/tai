package tai.criteria

import tai.base.JsonList
import tai.base.JsonMap
import tai.base.PrimitiveValue
import tai.base.assertThat
import tai.criteria.ex.TaiCriteriaException
import tai.criteria.operators.OperationMap
import tai.criteria.operators.op_
import java.lang.Exception
import java.util.*

data class SqlAndParams(
    val sql: String,
    val params: JsonList
);

interface CriteriaToTextConverter {
    fun convert(jsonMap: JsonMap): SqlAndParams;
}

class CriteriaToTextConverterImpl(
    val operationMap: OperationMap,
    private val criteriaDialectBuilder: CriteriaDialectBuilder
) : CriteriaToTextConverter {

    override fun convert(jsonMap: JsonMap): SqlAndParams {
        val paramsBuilder = ParamsBuilderImpl(mutableListOf());
        val rootCriteriaDialect = criteriaDialectBuilder.build(
            paramsBuilder
        );
        return SqlAndParams(
            toExpression(jsonMap, rootCriteriaDialect).toTextRepresentation(),
            paramsBuilder.build()
        );
    }

    private fun toExpression(jsonMap: JsonMap, rootCriteriaDialect: CriteriaDialect): CriteriaExpression {
        val operationName = jsonMap[op_] as String;
        val criteriaOperation = operationMap[operationName]

        validateCriteriaOperation(criteriaOperation);

        val criteriaDialect = CriteriaDialectWrapper(
            rootCriteriaDialect, jsonMap
        );

        return when (criteriaOperation) {
            is CriteriaOperationNative<*> -> callNative(criteriaOperation, criteriaDialect);
            is CriteriaOperation0 -> call0(criteriaOperation, criteriaDialect);
            is CriteriaOperation1 -> call1(criteriaOperation, criteriaDialect);
            is CriteriaOperation2 -> call2(criteriaOperation, criteriaDialect);
            is CriteriaOperation3 -> call3(criteriaOperation, criteriaDialect);
            is CriteriaOperation4 -> call4(criteriaOperation, criteriaDialect);
            is CriteriaOperation5 -> call5(criteriaOperation, criteriaDialect);
            is CriteriaOperation6 -> call6(criteriaOperation, criteriaDialect);
            is CriteriaOperation7 -> call7(criteriaOperation, criteriaDialect);
            is CriteriaOperation8 -> call8(criteriaOperation, criteriaDialect);
            else -> throw TaiCriteriaException("Unsupported criteria operation: $criteriaOperation");
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
            is CriteriaOperation6 -> assertLength(6, criteriaOperation);
            is CriteriaOperation7 -> assertLength(7, criteriaOperation);
            is CriteriaOperation8 -> assertLength(8, criteriaOperation);
            else -> throw TaiCriteriaException("Unsupported criteria operation: $criteriaOperation");
        }
    }

    private fun assertLength(expectedSize: Int, criteriaOperation: CriteriaOperation) {
        assertThat(criteriaOperation.paramSpecs.size == expectedSize) { "ParamSpecs.size should be $expectedSize for criteriaOperation: $criteriaOperation" };
    }

    private fun call8(
        criteriaOperation: CriteriaOperation8,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) = toCriteriaExpression8(
            criteriaDialectWrapper,
            criteriaOperation.paramSpecs
        );
        return criteriaOperation.renderExpression(
            criteriaDialectWrapper,
            arg1,
            arg2,
            arg3,
            arg4,
            arg5,
            arg6,
            arg7,
            arg8
        );
    }

    private fun call7(
        criteriaOperation: CriteriaOperation7,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3, arg4, arg5, arg6, arg7) = toCriteriaExpression7(
            criteriaDialectWrapper,
            criteriaOperation.paramSpecs
        );
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    private fun call6(
        criteriaOperation: CriteriaOperation6,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3, arg4, arg5, arg6) = toCriteriaExpression6(
            criteriaDialectWrapper,
            criteriaOperation.paramSpecs
        );
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    private fun call5(
        criteriaOperation: CriteriaOperation5,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3, arg4, arg5) = toCriteriaExpression5(
            criteriaDialectWrapper,
            criteriaOperation.paramSpecs
        );
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2, arg3, arg4, arg5);
    }

    private fun call4(
        criteriaOperation: CriteriaOperation4,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3, arg4) = toCriteriaExpression4(criteriaDialectWrapper, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2, arg3, arg4);
    }

    private fun call3(
        criteriaOperation: CriteriaOperation3,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2, arg3) = toCriteriaExpression3(criteriaDialectWrapper, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2, arg3);
    }

    private fun call2(
        criteriaOperation: CriteriaOperation2,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val (arg1, arg2) = toCriteriaExpression2(criteriaDialectWrapper, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg1, arg2);
    }

    private fun call1(
        criteriaOperation: CriteriaOperation1,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val arg: CriteriaExpression = toCriteriaExpression1(criteriaDialectWrapper, criteriaOperation.paramSpecs);
        return criteriaOperation.renderExpression(criteriaDialectWrapper, arg);
    }

    private fun call0(
        criteriaOperation: CriteriaOperation0,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        return criteriaOperation.renderExpression(criteriaDialectWrapper);
    }

    private fun callNative(
        criteriaOperation: CriteriaOperationNative<*>,
        criteriaDialectWrapper: CriteriaDialectWrapper
    ): CriteriaExpression {
        val jsonMap = criteriaDialectWrapper.ctxObject;
        val (name, isMandatory, defaultValue) = criteriaOperation.paramSpecs.iterator().next();
        val operatorName = jsonMap[op_] as String;

        val arg = if (isMandatory) {
            Objects.requireNonNull(
                jsonMap[name], valueRequiredMsg(name, operatorName)
            )
        } else {
            jsonMap.getOrDefault(
                name, Objects.requireNonNull(
                    defaultValue, noDefaultValueGivenMsg(name, operatorName)
                )
            );
        }

        assertThat(arg!!::class == criteriaOperation.parameterType) {
            invalidArgumentTypeMsg(
                arg,
                name,
                operatorName,
                criteriaOperation.parameterType.simpleName!!
            )
        }

        try {
            return (criteriaOperation as CriteriaOperationNative<PrimitiveValue>).renderExpression(criteriaDialectWrapper, arg);
        } catch (e: Exception) {
            print(e);
            return null!!;
        }
    }

    private fun invalidArgumentTypeMsg(arg: Any, name: String, operatorName: String, expectedType: String): String {
        return "Argument '$arg' with invalid type '${arg::class}' provided for parameter '$name' in operator '$operatorName', expected type = $expectedType";
    }

    private fun toCriteriaExpression8(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp8 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg4 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg5 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg6 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg7 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg8 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp8(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    private fun toCriteriaExpression7(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp7 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg4 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg5 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg6 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg7 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp7(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    private fun toCriteriaExpression6(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp6 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg4 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg5 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg6 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp6(arg1, arg2, arg3, arg4, arg5, arg6);
    }

    private fun toCriteriaExpression5(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp5 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg4 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg5 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp5(arg1, arg2, arg3, arg4, arg5);
    }

    private fun toCriteriaExpression4(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp4 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg4 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp4(arg1, arg2, arg3, arg4);
    }

    private fun toCriteriaExpression3(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp3 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg3 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp3(arg1, arg2, arg3);
    }

    private fun toCriteriaExpression2(
        criteriaDialectWrapper: CriteriaDialectWrapper,
        paramSpecs: Collection<ParamSpec>
    ): Exp2 {
        val iterator = paramSpecs.iterator();
        val arg1 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        val arg2 = argToExpression(criteriaDialectWrapper.ctxObject, criteriaDialectWrapper.root, iterator.next());
        return Exp2(arg1, arg2);
    }

    private fun toCriteriaExpression1(
        criteriaDialectWrapper: CriteriaDialectWrapper, paramSpecs: Collection<ParamSpec>
    ): CriteriaExpression {
        return argToExpression(
            criteriaDialectWrapper.ctxObject,
            criteriaDialectWrapper.root,
            paramSpecs.iterator().next()
        );
    }

    private fun argToExpression(
        jsonMap: JsonMap,
        rootCriteriaDialect: CriteriaDialect,
        paramSpec: ParamSpec
    ): CriteriaExpression {
        return when (paramSpec) {
            is ParamSpecSingle -> argToExpSingle(jsonMap, rootCriteriaDialect, paramSpec);
            is ParamSpecMulti -> argToExpMulti(jsonMap, rootCriteriaDialect, paramSpec);
        }
    }

    private fun argToExpSingle(
        jsonMap: JsonMap,
        rootCriteriaDialect: CriteriaDialect,
        paramSpec: ParamSpecSingle
    ): CriteriaExpression {
        val opJsonMap = getOpJsonForKey(jsonMap, paramSpec);
        if (opJsonMap !is Map<*, *>) {
            throwInvalidArgumentTypeEx(paramSpec.name, "Map", opJsonMap::class.simpleName!!, jsonMap);
        }
        return toExpression(opJsonMap as JsonMap, rootCriteriaDialect);
    }

    private fun argToExpMulti(
        jsonMap: JsonMap,
        rootCriteriaDialect: CriteriaDialect,
        paramSpec: ParamSpecMulti
    ): CriteriaExpression {
        val (name) = paramSpec;
        val opJsonList = getOpJsonForKey(jsonMap, paramSpec);
        if (opJsonList !is List<*>) {
            throwInvalidArgumentTypeEx(name, "List", opJsonList::class.simpleName!!, jsonMap);
        }
        return paramSpec.combineMulti(
            CriteriaDialectWrapper(rootCriteriaDialect, jsonMap),
            opJsonList.map { toExpression(it as JsonMap, rootCriteriaDialect) });
    }

    private fun getOpJsonForKey(jsonMap: JsonMap, paramSpec: ParamSpec): Any {
        val (name, isMandatory, defaultValue) = paramSpec;
        val operatorName = jsonMap[op_] as String;
        val opJsonMap = if (isMandatory) {
            jsonMap[name]
        } else {
            jsonMap.getOrDefault(
                name, Objects.requireNonNull(
                    defaultValue, noDefaultValueGivenMsg(name, operatorName)
                )
            );
        }
        return Objects.requireNonNull(opJsonMap, valueRequiredMsg(name, operatorName))!!;
    }

    private fun valueRequiredMsg(name: String, operatorName: String): String {
        return "No value given for parameter '$name' in operator '$operatorName'";
    }

    private fun noDefaultValueGivenMsg(name: String, operatorName: String): String {
        return "No default value given for parameter '$name' in operator '$operatorName'";
    }

    private fun throwInvalidArgumentTypeEx(
        name: String,
        expectedType: String,
        actualType: String,
        jsonMap: JsonMap
    ): Nothing {
        throw TaiCriteriaException("Invalid argument type for key '$name' in jsonMap = $jsonMap, expected type = $expectedType but actual type = $actualType");
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

data class Exp6(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression,
    val arg4: CriteriaExpression,
    val arg5: CriteriaExpression,
    val arg6: CriteriaExpression
);

data class Exp7(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression,
    val arg4: CriteriaExpression,
    val arg5: CriteriaExpression,
    val arg6: CriteriaExpression,
    val arg7: CriteriaExpression
);

data class Exp8(
    val arg1: CriteriaExpression,
    val arg2: CriteriaExpression,
    val arg3: CriteriaExpression,
    val arg4: CriteriaExpression,
    val arg5: CriteriaExpression,
    val arg6: CriteriaExpression,
    val arg7: CriteriaExpression,
    val arg8: CriteriaExpression
);

class CriteriaDialectWrapper(val root: CriteriaDialect, override val ctxObject: JsonMap) : CriteriaDialect by root {
}