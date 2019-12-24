package tai.sql

import kotlinx.coroutines.runBlocking
import org.junit.Test
import tai.criteria.ops.column
import tai.sql.impl.SqlExecutorImpl

class PaginationTest {

    @Test
    fun testPagination() {
        val sqlDB = createSqlDb(
            SqlExecutorImpl(
                createDataSource()
            )
        )

        runBlocking {
            val list = sqlDB.queryForArrays(
                SqlQuery(
                    selections = listOf(
                        column("id"),
                        column("username")
                    ),
                    from = listOf(
                        FromSpec(table = "users")
                    ),
                    pagination = SqlPagination(
                        paginationColumnSpec = ColumnSpec("id"),
                        size = 20,
                        offset = 0
                    )
                )
            )
            println("size: ${list.size}")
            println(list.joinToString { "$it\n" })
        }
    }
}