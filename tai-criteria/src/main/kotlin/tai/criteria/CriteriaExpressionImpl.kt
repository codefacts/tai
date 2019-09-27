package tai.criteria

import java.lang.StringBuilder
import java.util.function.Consumer

class CriteriaExpressionImpl(val criteriaExpressions: List<CriteriaExpression>) : CriteriaExpression {

    override fun toInternalRepresentation(stringBuilder: StringBuilder): StringBuilder {
        criteriaExpressions.forEach(Consumer { exp -> exp.toInternalRepresentation(stringBuilder) })
        return stringBuilder;
    }

    override fun toTextRepresentation(): String {
        return toInternalRepresentation(StringBuilder()).toString();
    }
}

class CriteriaExpressionBuilderImpl : CriteriaExpressionBuilder {
    private val list = mutableListOf<CriteriaExpression>();

    override fun add(text: String): CriteriaExpressionBuilder {
        list.add(CriteExp(text));
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

private class CriteExp(val text: String) : CriteriaExpression {
    override fun toInternalRepresentation(stringBuilder: StringBuilder): StringBuilder {
        return stringBuilder.append(text);
    }

    override fun toTextRepresentation(): String {
        return text;
    }
}