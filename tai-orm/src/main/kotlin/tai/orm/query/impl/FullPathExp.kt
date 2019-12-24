package tai.orm.query.impl

import tai.orm.JoinParam
import tai.orm.core.PathExpression

internal fun createAliasToJoinParamMap(joinParams: Collection<JoinParam>): Map<String, JoinParam> {
    return joinParams.asSequence().map { it.alias to it }.toMap()
}

internal fun createAliasToFullPathExpMap(
    rootAlias: String,
    joinParams: Collection<JoinParam>,
    aliasToJoinParamMap: Map<String, JoinParam>
): Map<String, PathExpression> {

    return joinParams.map { it.alias to toFullPathExp(
        it.path,
        rootAlias,
        aliasToJoinParamMap
    )
    }.toMap()
}

private fun toFullPathExp(
    path: PathExpression,
    rootAlias: String,
    aliasToJoinParamMap: Map<String, JoinParam>
): PathExpression {
    val paths = generateSequence(path) {
        if (it.root() == rootAlias) {
            return@generateSequence null
        }
        return@generateSequence aliasToJoinParamMap[it.root()]!!.path
    }.map { it.subPath(1) }.toList().reversed()
    return PathExpression.create(rootAlias).concat(paths)
}

