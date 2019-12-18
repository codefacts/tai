package tai.orm.read

data class FieldAndIndexPair(
    val field: String,
    val index: Int
)

data class DirectRelation(
    val field: String,
    val readDirectRelation: ReadDirectRelation
)

data class IndirectRelation(
    val field: String,
    val indirectRelationReader: ReadIndirectRelation
)