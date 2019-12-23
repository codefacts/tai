package qorm

import kotlinx.coroutines.runBlocking
import org.junit.Test
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.operators.OperationMapImpl
import tai.orm.Pagination
import tai.orm.QueryParam
import tai.orm.core.FieldExpression
import test.orm.entity_config.Entities
import tai.orm.entity.EntityUtils
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.field_
import tai.orm.query.impl.FieldExpOperator
import tai.orm.query.impl.QueryExecutorImpl
import tai.sql.impl.BaseSqlDBImpl
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlDialectImpl
import tai.sql.impl.SqlExecutorImpl
import javax.sql.DataSource

class QueryTest {

    @Test
    fun testToSqlQuery() {

        val opMap = OperationMapImpl(
            OPERATION_MAP.operationMap + mapOf(
                field_ to FieldExpOperator()
            )
        )

        val entities = EntityUtils.validateAndPreProcess(Entities.entities())
        val helper = EntityMappingHelperImpl(entities)
        val baseSqlDB = BaseSqlDBImpl(
            CoreSqlDBImpl(
                SqlExecutorImpl(createDateSource()),
                CriteriaToTextConverterImpl(
                    opMap, CriteriaDialectBuilderImpl()
                )
            ), SqlDialectImpl()
        )
        val queryExecutor = QueryExecutorImpl(helper, baseSqlDB)
        runBlocking {
            val list = queryExecutor.findAll(
                QueryParam(
                    entity = Entities.USER_ENTITY,
                    alias = "r",
                    selections = listOf(
                        FieldExpression.create("r", "id"),
                        FieldExpression.create("r", "username"),
                        FieldExpression.create("r", "password")
                    ),
                    pagination = Pagination(
                        fieldExpression = FieldExpression.create("r", "id"),
                        offset = 25,
                        size = 20
                    )
                )
            )
            println(list)
        }
    }

    private fun createDateSource(): DataSource {
        return getMySQLDataSource()
    }
}

