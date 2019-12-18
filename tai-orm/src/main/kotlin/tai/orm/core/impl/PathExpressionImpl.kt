package tai.orm.core.impl

import tai.orm.core.PathExpression
import tai.orm.core.ex.PathExpressionException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

/**
 * Created by Jango on 17/02/10.
 */
class PathExpressionImpl : PathExpression {
    val parts: List<String>

    constructor(pathExpression: String) {
        Objects.requireNonNull(pathExpression)
        val list =
            Stream.of(*pathExpression.split(".").toTypedArray())
                .filter { s: String -> !s.isEmpty() }
                .collect(Collectors.toList())
        if (list.size < 1) {
            throw PathExpressionException("No elements found in path Expression '$pathExpression'")
        }
        parts = list.toList()
    }

    constructor(parts: List<String>) {
        this.parts = parts
    }

    override val parent: PathExpression?
        get() = if (parts.size < 2) {
            null
        } else PathExpressionImpl(parts.subList(0, parts.size - 1))

    override fun parts(): List<String> {
        return parts
    }

    override fun getAt(index: Int): String {
        return parts[index]
    }

    override fun size(): Int {
        return parts.size
    }

    override val isEmpty: Boolean
        get() = parts.isEmpty()

    override fun root(): String {
        return parts[0]
    }

    override fun last(): String {
        return parts[parts.size - 1]
    }

    override fun subPath(fromIndex: Int, toIndex: Int): PathExpression {
        return PathExpressionImpl(parts.subList(fromIndex, toIndex))
    }

    override fun subPath(fromIndex: Int): PathExpression {
        return subPath(fromIndex, size())
    }

    override fun startsWith(rootAlias: String): Boolean {
        return root() == rootAlias
    }

    override fun concat(vararg pathExpression: PathExpression): PathExpression {
        return PathExpressionImpl(
            this.parts + pathExpression.flatMap { it.parts() }
        )
    }

    override fun concat(pathExpressions: List<PathExpression>): PathExpression {
        return PathExpressionImpl(
            this.parts + pathExpressions.flatMap { it.parts() }
        )
    }

    override fun concat(vararg parts: String): PathExpression {
        return PathExpressionImpl(
            this.parts + parts
        )
    }

    override fun concat(vararg parts: List<String>): PathExpression {
        return PathExpressionImpl(
            this.parts + parts.toList().flatten()
        )
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PathExpressionImpl
        return parts == that.parts
    }

    override fun hashCode(): Int {
        return parts.hashCode()
    }

    override fun toString(): String {
        return parts.stream().collect(Collectors.joining("."))
    }
}