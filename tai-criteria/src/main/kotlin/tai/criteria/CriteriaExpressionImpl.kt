package tai.criteria

import java.lang.StringBuilder
import java.util.function.Consumer

class CriteriaExpressionBuilderImpl : CriteriaExpressionBuilder {
    private val list = mutableListOf<CriteriaExpression>();

    override fun add(text: String): CriteriaExpressionBuilder {
        list.add(CritExpSingle(text));
        return this;
    }

    override fun add(criteriaExpression: CriteriaExpression): CriteriaExpressionBuilder {
        list.add(criteriaExpression);
        return this;
    }

    override fun build(): CriteriaExpression {
        return CriteriaExpressionImpl(list.toList());
    }
}

data class CriteriaExpressionImpl(val criteriaExpressions: List<CriteriaExpression>) : CriteriaExpression {
    override val isBlank: Boolean = criteriaExpressions.isEmpty();

    override fun toInternalRepresentation(stringBuilder: StringBuilder): StringBuilder {
        criteriaExpressions.forEach(Consumer { exp -> exp.toInternalRepresentation(stringBuilder) })
        return stringBuilder;
    }

    override fun toTextRepresentation(): String {
        return toInternalRepresentation(StringBuilder()).toString();
    }

    override fun toString(): String {
        return criteriaExpressions.joinToString("")
    }
}

data class CritExpSingle(val text: String) : CriteriaExpression {
    override val isBlank: Boolean = text.isBlank();

    override fun toInternalRepresentation(stringBuilder: StringBuilder): StringBuilder {
        return stringBuilder.append(text);
    }

    override fun toTextRepresentation(): String {
        return text;
    }

    override fun toString(): String {
        return text;
    }
}