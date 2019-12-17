package tai.orm.core

import tai.orm.core.impl.FieldExpressionImpl

/**
 * Created by Jango on 17/02/09.
 */
interface FieldExpression {
    val parent: PathExpression
    val field: String

    fun toPathExpression(): PathExpression
    fun size(): Int

    companion object {
        fun create(pathExpression: PathExpression): FieldExpression {
            return FieldExpressionImpl(pathExpression)
        }

        fun create(vararg parts: String): FieldExpression {
            return FieldExpressionImpl(*parts)
        }

        fun create(pathExpression: String): FieldExpression {
            return FieldExpressionImpl(pathExpression)
        }
    }
}