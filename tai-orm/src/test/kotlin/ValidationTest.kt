import org.junit.Test
import tai.orm.entity.*
import tai.orm.entity.columnmapping.ColumnMapping
import tai.orm.entity.columnmapping.DirectRelationMappingImpl
import tai.orm.validation.ex.EntityValidationException

class ValidationTest {

    @Test(expected = EntityValidationException::class)
    fun testColumnMappingMissing() {
        val user = Entity(
            name = "User",
            primaryKey = "id",
            fields = listOf(
                Field("id"),
                Field("name")
            ),
            dbMapping = DbMapping(
                table = "users",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id")
//                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            user
        ))
    }

    @Test(expected = EntityValidationException::class)
    fun testCorrespondingFieldMissing() {
        val user = Entity(
            name = "User",
            primaryKey = "id",
            fields = listOf(
                Field("id")
//                Field("name")
            ),
            dbMapping = DbMapping(
                table = "users",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            user
        ))
    }

    @Test(expected = EntityValidationException::class)
    fun testPrimaryKeyAbsentInFields() {
        val user = Entity(
            name = "User",
            primaryKey = "id",
            fields = listOf(
//                Field("id")
                Field("name")
            ),
            dbMapping = DbMapping(
                table = "users",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            user
        ))
    }

    @Test(expected = EntityValidationException::class)
    fun testPrimaryColumnAbsentInColumnMappings() {
        val user = Entity(
            name = "User",
            primaryKey = "id",
            fields = listOf(
//                Field("id")
                Field("name")
            ),
            dbMapping = DbMapping(
                table = "users",
                primaryColumn = "idx",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            user
        ))
    }

    @Test
    fun testNoExceptionOnValidEntity() {
        val user = Entity(
            name = "A",
            primaryKey = "id",
            fields = listOf(
                Field("id"),
                Field("name")
            ),
            dbMapping = DbMapping(
                table = "a",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            user
        ))
    }

    @Test
    fun testCorrespondingRelationMappingMissing() {
        val a = Entity(
            name = "A",
            primaryKey = "id",
            fields = listOf(
                Field("id"),
                Field("name"),
                Field("b", Relationship(
                    Relationship.Name.HAS_ONE, "B"
                ))
            ),
            dbMapping = DbMapping(
                table = "a",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf(
                    DirectRelationMappingImpl(
                        field = "b",
                        referencingEntity = "B",
                        referencingTable = "b",
                        foreignColumnMappingList = listOf(
                            ForeignColumnMapping("b_id", "id"),
                            ForeignColumnMapping("b_name", "name")
                        )
                    )
                )
            )
        )

        val b = Entity(
            name = "B",
            primaryKey = "id",
            fields = listOf(
                Field("id"),
                Field("name")
            ),
            dbMapping = DbMapping(
                table = "b",
                primaryColumn = "id",
                columnMappings = listOf(
                    ColumnMapping("id", "id"),
                    ColumnMapping("name", "name")
                ),
                relationMappings = listOf()
            )
        )

        EntityUtils.validateAndPreProcess(listOf(
            a, b
        ))
    }
}