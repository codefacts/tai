package tai.sql

import kotlinx.coroutines.runBlocking
import org.junit.Test
import tai.criteria.operators.Order
import tai.criteria.ops.*
import tai.sql.impl.SqlExecutorImpl

class PaginationTest {

//    @Test
    fun testPagination() {
        val sqlDB = createSqlDb(
            SqlExecutorImpl(
                createDataSource()
            )
        )

        runBlocking {
            val list = sqlDB.queryArrays(
                SqlQuery(
                    selections = listOf(
                        column("u", "id"),
                        column("u","username")
                    ),
                    from = listOf(
                        FromSpec(table = "users", alias = "u")
                    ),
                    orderBy = listOf(
                        OrderBySpec("u", "username", Order.DESC)
                    ),
                    pagination = SqlPagination(
                        paginationColumnSpec = ColumnSpec("u", "username"),
                        size = 20,
                        offset = 0
                    )
                )
            )
            println("size: ${list.size}")
            println(list.joinToString { "$it\n" })
        }
    }

//    @Test
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