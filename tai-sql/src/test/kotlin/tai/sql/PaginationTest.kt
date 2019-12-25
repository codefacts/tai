package tai.sql

import kotlinx.coroutines.runBlocking
import org.junit.Test
import tai.criteria.ops.*
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlExecutorImpl

class PaginationTest {


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

    @Test
    fun testPagination2() {
        val coreSqlDB = createCoreSqlDB(
            SqlExecutorImpl(
                createDataSource()
            )
        )
        runBlocking {

            val frm = listOf(
                select(
                    column("id"),
                    column("username")
                ),
                from(
                    table("users")
                )
            )

            val rs = coreSqlDB.query(joinExpressions(frm))

            println(rs)
        }
    }
}