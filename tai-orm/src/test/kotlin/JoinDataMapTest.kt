import org.junit.Assert
import org.junit.Test
import tai.base.TextAndCollection
import tai.base.prettyPrint
import tai.criteria.operators.JoinType
import tai.orm.JoinParam
import tai.orm.core.PathExpression
import tai.orm.entity.DbMapping
import tai.orm.entity.Entity
import tai.orm.entity.Field
import tai.orm.entity.Relationship
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.query.CreateAliasIsLast
import tai.orm.query.impl.JoinData
import tai.orm.query.impl.JoinDataHelper
import tai.orm.query.impl.makeCreateAlias

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
            PathExpression.parse("r.field_a.field_b.field_c"), JoinParam(PathExpression.parse("r.field_a.field_b.field_c"), "k", JoinType.INNER_JOIN),
            joinDataMap, createAlias = { shortCode, isLast -> if (isLast) "k" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_a.field_a.field_a"), JoinParam(PathExpression.parse("r.field_a.field_a.field_a"), "q", JoinType.LEFT_JOIN),
            joinDataMap, createAlias = { shortCode, isLast -> if (isLast) "q" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_a.field_a.field_c"), JoinParam(PathExpression.parse("r.field_a.field_a.field_c"), "w", JoinType.RIGHT_JOIN),
            joinDataMap, createAlias = { shortCode, isLast -> if (isLast) "w" else createAlias(shortCode) }
        )

        joinDataHelper.populateJoinDataMap(
            "r", helper.getEntity("B"),
            PathExpression.parse("r.field_b.field_a.field_b"), JoinParam(PathExpression.parse("r.field_b.field_a.field_b"), "n", JoinType.FULL_JOIN),
            joinDataMap, createAlias = { shortCode, isLast -> if (isLast) "n" else createAlias(shortCode) }
        )

        println(ae)

        val pretty = prettyPrint(joinDataMap) {
            return@prettyPrint if (it is JoinData) TextAndCollection(it.copy(joinDataMap = mutableMapOf()).toString(), it.joinDataMap) else TextAndCollection("", it)
        }.trim()

        println(pretty)

        Assert.assertEquals("""
            {
              field_a
                JoinData(parentEntityAlias='r', childEntityAlias='a_1', parentEntity=B, childEntityField='field_a', childEntity=A, JoinType=null)
                {
                  field_b
                    JoinData(parentEntityAlias='a_1', childEntityAlias='b_1', parentEntity=A, childEntityField='field_b', childEntity=B, JoinType=null)
                    {
                      field_c
                        JoinData(parentEntityAlias='b_1', childEntityAlias='k', parentEntity=B, childEntityField='field_c', childEntity=C, JoinType=INNER_JOIN)
                        {
                        }
                    }
                  field_a
                    JoinData(parentEntityAlias='a_1', childEntityAlias='a_2', parentEntity=A, childEntityField='field_a', childEntity=A, JoinType=null)
                    {
                      field_a
                        JoinData(parentEntityAlias='a_2', childEntityAlias='q', parentEntity=A, childEntityField='field_a', childEntity=A, JoinType=LEFT_JOIN)
                        {
                        }
                      field_c
                        JoinData(parentEntityAlias='a_2', childEntityAlias='w', parentEntity=A, childEntityField='field_c', childEntity=C, JoinType=RIGHT_JOIN)
                        {
                        }
                    }
                }
              field_b
                JoinData(parentEntityAlias='r', childEntityAlias='b_2', parentEntity=B, childEntityField='field_b', childEntity=B, JoinType=null)
                {
                  field_a
                    JoinData(parentEntityAlias='b_2', childEntityAlias='a_3', parentEntity=B, childEntityField='field_a', childEntity=A, JoinType=null)
                    {
                      field_b
                        JoinData(parentEntityAlias='a_3', childEntityAlias='n', parentEntity=A, childEntityField='field_b', childEntity=B, JoinType=FULL_JOIN)
                        {
                        }
                    }
                }
            }
        """.trimIndent(), pretty)

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