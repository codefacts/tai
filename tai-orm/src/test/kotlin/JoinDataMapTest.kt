import org.junit.Assert
import org.junit.Test
import tai.base.TextAndCollection
import tai.base.prettyPrint
import tai.orm.core.PathExpression
import tai.orm.entity.DbMapping
import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.Relationship
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.impl.JoinData
import tai.orm.impl.JoinDataHelper
import tai.orm.impl.makeCreateAlias

class JoinDataMapTest {

    @Test
    fun test() {
        val helper = EntityMappingHelperImpl(entities())
        val joinDataHelper = JoinDataHelper(
            helper
        )
        println(joinDataHelper)

        val joinDataMap = mutableMapOf<String, JoinData>()
        val createAlias = makeCreateAlias()

        val ae = joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_a.field_b.field_c"),
            joinDataMap, createAlias = {shortCode, isLast -> if (isLast) "k" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_a.field_a.field_a"),
            joinDataMap, createAlias = {shortCode, isLast -> if (isLast) "q" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_a.field_a.field_c"),
            joinDataMap, createAlias = {shortCode, isLast -> if (isLast) "w" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_b.field_a.field_b"),
            joinDataMap, createAlias = {shortCode, isLast -> if (isLast) "n" else createAlias(shortCode) }
        )

        println(ae)

        val pretty = prettyPrint(joinDataMap) {
            return@prettyPrint if (it is JoinData) TextAndCollection(it.copy(joinDataMap = mutableMapOf()).toString(), it.joinDataMap) else TextAndCollection("", it)
        }.trim()

        println(pretty)

        Assert.assertEquals("{\n" +
                "  field_a\n" +
                "    JoinData(parentEntityAlias='r', childEntityAlias='a1', parentEntity=B, childEntityField='field_a', childEntity=A, joinDataMap={})\n" +
                "    {\n" +
                "      field_b\n" +
                "        JoinData(parentEntityAlias='a1', childEntityAlias='b1', parentEntity=A, childEntityField='field_b', childEntity=B, joinDataMap={})\n" +
                "        {\n" +
                "          field_c\n" +
                "            JoinData(parentEntityAlias='b1', childEntityAlias='k', parentEntity=B, childEntityField='field_c', childEntity=C, joinDataMap={})\n" +
                "            {\n" +
                "            }\n" +
                "        }\n" +
                "      field_a\n" +
                "        JoinData(parentEntityAlias='a1', childEntityAlias='a2', parentEntity=A, childEntityField='field_a', childEntity=A, joinDataMap={})\n" +
                "        {\n" +
                "          field_a\n" +
                "            JoinData(parentEntityAlias='a2', childEntityAlias='q', parentEntity=A, childEntityField='field_a', childEntity=A, joinDataMap={})\n" +
                "            {\n" +
                "            }\n" +
                "          field_c\n" +
                "            JoinData(parentEntityAlias='a2', childEntityAlias='w', parentEntity=A, childEntityField='field_c', childEntity=C, joinDataMap={})\n" +
                "            {\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "  field_b\n" +
                "    JoinData(parentEntityAlias='r', childEntityAlias='b2', parentEntity=B, childEntityField='field_b', childEntity=B, joinDataMap={})\n" +
                "    {\n" +
                "      field_a\n" +
                "        JoinData(parentEntityAlias='b2', childEntityAlias='a3', parentEntity=B, childEntityField='field_a', childEntity=A, joinDataMap={})\n" +
                "        {\n" +
                "          field_b\n" +
                "            JoinData(parentEntityAlias='a3', childEntityAlias='n', parentEntity=A, childEntityField='field_b', childEntity=B, joinDataMap={})\n" +
                "            {\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}", pretty)

    }

    private fun entities(): Collection<Entity> {
        val a = Entity(
            name = "A",
            primaryKey = "",
            fields = listOf(
                Field("field_a", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "A"
                )),
                Field("field_b", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "B"
                )),
                Field("field_c", Relationship(
                    name = Relationship.Name.HAS_MANY,
                    entity = "C"
                ))
            ),
            dbMapping = createDbMapping("a")
        )

        val b = Entity(
            name = "B",
            primaryKey = "",
            fields = listOf(
                Field("field_a", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "A"
                )),
                Field("field_b", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "B"
                )),
                Field("field_c", Relationship(
                    name = Relationship.Name.HAS_MANY,
                    entity = "C"
                ))
            ),
            dbMapping = createDbMapping("b")
        )

        val c = Entity(
            name = "C",
            primaryKey = "",
            fields = listOf(
                Field("field_a", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "A"
                )),
                Field("field_b", Relationship(
                    name = Relationship.Name.HAS_ONE,
                    entity = "B"
                )),
                Field("field_c", Relationship(
                    name = Relationship.Name.HAS_MANY,
                    entity = "C"
                ))
            ),
            dbMapping = createDbMapping("c")
        )

        return listOf(a, b, c)
    }

    private fun createDbMapping(tableShortCode: String): DbMapping {
        return DbMapping(
            table = "",
            primaryColumn = "",
            tableShortCode = tableShortCode,
            columnMappings = listOf(),
            relationMappings = listOf()
        )
    }
}