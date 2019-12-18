import org.junit.Test
import tai.orm.core.FieldExpression
import tai.orm.core.PathExpression
import tai.orm.entity.*
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.read.PathInfo
import tai.orm.read.ReaderBuilder

class ReadTest {
    val helper = EntityMappingHelperImpl(entities())

    @Test
    fun test() {

        val fieldMap = listOf(
            FieldExpression.parse("r.a_id"),
            FieldExpression.parse("r.a_name"),
            FieldExpression.parse("r.a.a_id"),
            FieldExpression.parse("r.a.a_name"),
            FieldExpression.parse("r.b.b_id"),
            FieldExpression.parse("r.b.a.a_id"),
            FieldExpression.parse("r.c.c_id"),
            FieldExpression.parse("r.c.b.b_id"),
            FieldExpression.parse("r.c.b.b_name"),
            FieldExpression.parse("r.aList.a_id"),
            FieldExpression.parse("r.aList.a_name"),
            FieldExpression.parse("r.aList.a.a_id"),
            FieldExpression.parse("r.aList.b.b_id"),
            FieldExpression.parse("r.aList.b.b_name")
        ).mapIndexed { index, fieldExpression -> fieldExpression to index }.toMap()

        val readerBuilder = ReaderBuilder(
            fieldExpressionToIndexMap = fieldMap,
            rootAlias = "r",
            rootEntity = "A",
            helper = helper,
            aliasToFullPathExpressionMap = mapOf()
        )

        println(readerBuilder)

        val map: Map<PathExpression, PathInfo> = readerBuilder.build()

        println("fieldMap: " + fieldMap.map { it.toString() + "\n" })
        println(map.map { it.toString() + "\n" })

//        Assert.assertEquals(map, mapOf(
//            PathExpression.parse("r.a") to
//            PathInfo(
//                rootEntity = "A",
//                helper = helper,
//                fieldAndIndexPairs = mutableSetOf(
//                ),
//                directRelations = mutableSetOf(),
//                indirectRelations = mutableSetOf(),
//                primaryKeyIndex = -1
//            )
//        ))

        println()
    }

    private fun entities(): List<Entity> {
        val a = Entity(
            "A",
            "a_id",
            fields = listOf(
                Field("a_id"),
                Field("a_name"),
                Field("a", Relationship(
                    Relationship.Name.HAS_ONE, "A"
                )),
                Field("b", Relationship(
                    Relationship.Name.HAS_ONE, "B"
                )),
                Field("c", Relationship(
                    Relationship.Name.HAS_ONE, "C"
                )),
                Field("aList", Relationship(
                    Relationship.Name.HAS_MANY, "A"
                )),
                Field("bList", Relationship(
                    Relationship.Name.HAS_MANY, "B"
                )),
                Field("cList", Relationship(
                    Relationship.Name.HAS_MANY, "C"
                ))
            ),
            dbMapping = DbMapping(
                table = "table_a",
                primaryColumn = "a_id",
                tableShortCode = "shortcode_a",
                columnMappings = listOf(),
                relationMappings = listOf()
            )
        )
        val b = Entity(
            "B",
            "b_id",
            fields = listOf(
                Field("b_id"),
                Field("b_name"),
                Field("a", Relationship(
                    Relationship.Name.HAS_ONE, "A"
                )),
                Field("b", Relationship(
                    Relationship.Name.HAS_ONE, "B"
                )),
                Field("c", Relationship(
                    Relationship.Name.HAS_ONE, "C"
                )),
                Field("aList", Relationship(
                    Relationship.Name.HAS_MANY, "A"
                )),
                Field("bList", Relationship(
                    Relationship.Name.HAS_MANY, "B"
                )),
                Field("cList", Relationship(
                    Relationship.Name.HAS_MANY, "C"
                ))
            ),
            dbMapping = DbMapping(
                table = "table_b",
                primaryColumn = "b_id",
                tableShortCode = "shortcode_b",
                columnMappings = listOf(),
                relationMappings = listOf()
            )
        )
        val c = Entity(
            "C",
            "c_id",
            fields = listOf(
                Field("c_id"),
                Field("c_name"),
                Field("a", Relationship(
                    Relationship.Name.HAS_ONE, "A"
                )),
                Field("b", Relationship(
                    Relationship.Name.HAS_ONE, "B"
                )),
                Field("c", Relationship(
                    Relationship.Name.HAS_ONE, "C"
                )),
                Field("aList", Relationship(
                    Relationship.Name.HAS_MANY, "A"
                )),
                Field("bList", Relationship(
                    Relationship.Name.HAS_MANY, "B"
                )),
                Field("cList", Relationship(
                    Relationship.Name.HAS_MANY, "C"
                ))
            ),
            dbMapping = DbMapping(
                table = "table_c",
                primaryColumn = "c_id",
                tableShortCode = "shortcode_c",
                columnMappings = listOf(),
                relationMappings = listOf()
            )
        )
        return listOf(a, b, c)
    }
}