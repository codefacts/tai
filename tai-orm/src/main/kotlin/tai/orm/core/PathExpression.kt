package tai.orm.core

import tai.orm.core.impl.PathExpressionImpl
import java.util.*

/**
 * Created by Jango on 17/02/09.
 */
interface PathExpression {
    val parent: PathExpression?
    fun parts(): List<String>
    fun getAt(index: Int): String
    fun size(): Int
    val isEmpty: Boolean
    fun root(): String
    fun last(): String
    fun subPath(fromIndex: Int, toIndex: Int): PathExpression
    fun subPath(fromIndex: Int): PathExpression
    fun startsWith(rootAlias: String): Boolean
    fun concat(vararg pathExpression: PathExpression): PathExpression
    fun concat(pathExpressions: List<PathExpression>): PathExpression
    fun concat(vararg parts: String): PathExpression
    fun concat(vararg parts: List<String>): PathExpression

    companion object {
        fun parseAndCreate(path: String): PathExpression? {
            return PathExpressionImpl(path)
        }

        fun create(vararg parts: String): PathExpression {
            return PathExpressionImpl(parts.toList())
        }

        fun create(vararg partsList: List<String>): PathExpression {
            return PathExpressionImpl(partsList.toList().flatten())
        }

        fun create(vararg partsList: Array<String>): PathExpression {
            return PathExpressionImpl(partsList.flatMap { it.toList() })
        }

        fun create(pathExpList: List<PathExpression>): PathExpression {
            return PathExpressionImpl(pathExpList.flatMap { it.parts() })
        }
    }
}