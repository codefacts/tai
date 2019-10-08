package tai.criteria.operators

enum class Order(val value: String) {
    ASC("ASC"), DESC("DESC")
}

enum class JoinType(val value: String) {
    JOIN("JOIN"),
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    FULL_JOIN("FULL JOIN")
}

enum class ComparisonModifier(val value: String) {
    ALL("ALL"), ANY("ANY"), SOME("SOME")
}