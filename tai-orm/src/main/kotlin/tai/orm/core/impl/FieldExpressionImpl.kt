package tai.orm.core.impl

import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.core.ex.FieldExpressionException
import java.lang.NullPointerException

/**
 * Created by Jango on 17/02/10.
 */
class FieldExpressionImpl : FieldExpression {
    val pathExpression: PathExpression

    constructor(pathExpression: PathExpression) {
        if (pathExpression.size() < 2) {
            throw FieldExpressionException("At least 2 elements expected in path expression but less element found in $pathExpression")
        }
        this.pathExpression = pathExpression
    }

    constructor(vararg parts: String) : this(java.lang.String.join(".", *parts)) {}

    constructor(pathExpression: String) {
        this.pathExpression = PathExpressionImpl(pathExpression)
        if (this.pathExpression.size() < 2) {
            throw FieldExpressionException("At least 2 elements expected in path expression but less element found in '$pathExpression'")
        }
    }

    override val parent: PathExpression
        get() = pathExpression.parent!!

    override val field: String
        get() = pathExpression.last()

    override fun toPathExpression(): PathExpression {
        return pathExpression
    }

    override fun size(): Int {
        return pathExpression.size()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as FieldExpressionImpl
        return pathExpression.equals(that.pathExpression)
    }

    override fun hashCode(): Int {
        return pathExpression.hashCode() ?: 0
    }

    override fun toString(): String {
        return pathExpression.toString()
    }
}