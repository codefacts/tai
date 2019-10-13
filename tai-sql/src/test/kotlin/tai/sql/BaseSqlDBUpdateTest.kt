package tai.sql

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import tai.base.JsonList
import tai.base.PrimitiveValue
import tai.criteria.operators.JoinType
import tai.criteria.ops.*
import tai.sql.impl.UpdateResultImpl

interface BaseTable {
    companion object {
        val id = "id"
        val created_by = "created_by"
        val updated_by = "updated_by"
        val create_date = "create_date"
        val update_date = "update_date"
    }
}

interface UserTable {
    companion object : BaseTable {
        val username = "username"
        val email = "email"
        val phone = "phone"
        val address = "address"
        val password = "password"
        val date_of_birth = "date_of_birth"
        val first_name = "first_name"
        val last_name = "last_name"
        val gender = "gender"
        val picture_uri = "picture_uri"
        val join_date = "join_date"
        val user_type = "user_type"
        val active = "active"
    }
}

const val TEST_DB_NAME = "test_sales_db";

class BaseSqlDBUpdateTest {

    @Test
    fun executeSqlOperationInsertTest() {

        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();
            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            val updateResult = sqlDB.execute(
                SqlInsert(
                    database = "test_sales_db",
                    table = "users",
                    data = mapOf(
                        BaseTable.id to "11",
                        UserTable.first_name to "Khan",
                        UserTable.email to "hamara@email.hey",
                        UserTable.password to "123",
                        UserTable.active to 1,
                        UserTable.username to "Khan",
                        UserTable.phone to "012548751124"
                    )
                )
            );
            println(SQL)
            println(PARAMS)
            Assert.assertEquals(
                "INSERT INTO test_sales_db.users (id, first_name, email, password, active, username, phone) VALUES (?, ?, ?, ?, 1, ?, ?)",
                SQL
            )
            Assert.assertEquals(
                listOf(
                    "11", "Khan", "hamara@email.hey", "123", "Khan", "012548751124"
                ),
                PARAMS
            )
        }
    }

    @Test
    fun executeSqlOperationUpdateTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();
            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            val update = sqlDB.execute(
                SqlUpdate(
                    database = "test_sales_db",
                    table = "users",
                    data = mapOf(
                        UserTable.phone to "015872412",
                        UserTable.password to "1542"
                    ),
                    sqlConditions = listOf(
                        SqlCondition(
                            UserTable.user_type, "sec"
                        ),
                        SqlCondition(
                            UserTable.username, "Khan"
                        )
                    )
                )
            )
            println(SQL)
            println(PARAMS)
            Assert.assertEquals(
                "UPDATE test_sales_db.users SET phone = ?, password = ? WHERE (user_type = ? AND username = ?)",
                SQL
            )
            Assert.assertEquals(
                listOf(
                    "015872412", "1542", "sec", "Khan"
                ),
                PARAMS
            )
        }
    }

    @Test
    fun executeSqlOperationDeleteTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();
            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            val update = sqlDB.execute(
                SqlDelete(
                    database = "test_sales_db",
                    table = "users",
                    sqlConditions = listOf(
                        SqlCondition(UserTable.user_type, "sec"),
                        SqlCondition(UserTable.username, "Khan")
                    )
                )
            )
            println(SQL)
            println(PARAMS)
            Assert.assertEquals(
                "DELETE FROM test_sales_db.users WHERE ((user_type = ?) AND (username = ?))",
                SQL
            )
            Assert.assertEquals(
                listOf("sec", "Khan"),
                PARAMS
            )
        }
    }

    @Test
    fun updateOpWithoutFromAndWithoutWhereTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();

            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            sqlDB.update(
                SqlUpdateOp(
                    tables = listOf(FromSpec(database = TEST_DB_NAME, table = "users")),
                    values = listOf(
                        ColumnAndValue(UserTable.phone, valueOf("01985421218")),
                        ColumnAndValue(UserTable.password, valueOf("123"))
                    )
                )
            )

            println(SQL)
            println(PARAMS)

            Assert.assertEquals(
                "UPDATE test_sales_db.users SET phone = ?, password = ?",
                SQL.trim()
            )
            Assert.assertEquals(
                listOf("01985421218", "123"),
                PARAMS
            )
        }
    }

    @Test
    fun updateOpWithoutFromAndWithWhereTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();

            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            sqlDB.update(
                SqlUpdateOp(
                    tables = listOf(
                        FromSpec(
                            database = TEST_DB_NAME,
                            table = "users"
                        )
                    ),
                    values = listOf(
                        ColumnAndValue(UserTable.phone, valueOf("01985421218")),
                        ColumnAndValue(UserTable.password, valueOf("123"))
                    ),
                    where = listOf(
                        eq(column(UserTable.user_type), valueOf("sec")),
                        eq(column(UserTable.username), valueOf("Khan"))
                    )
                )
            )

            println(SQL)
            println(PARAMS)

            Assert.assertEquals(
                "UPDATE test_sales_db.users SET phone = ?, password = ? WHERE ((user_type = ?) AND (username = ?))",
                SQL.trim()
            )
            Assert.assertEquals(
                listOf("01985421218", "123", "sec", "Khan"),
                PARAMS
            )
        }
    }

    @Test
    fun updateOpWithFromAndWithWhereTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();

            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            sqlDB.update(
                SqlUpdateOp(
                    tables = listOf(
                        FromSpec(
                            table = "table_a"
                        )
                    ),
                    values = listOf(
                        ColumnAndValue(
                            column("table_a", "col_1"), column("table_b", "col_1")
                        ),
                        ColumnAndValue(
                            column("table_a", "col_2"), column("table_b", "col_2")
                        )
                    ),
                    from = listOf(
                        FromSpec(
                            database = TEST_DB_NAME,
                            table = "some_table",
                            alias = "table_a",
                            joins = listOf(
                                JoinSpec(
                                    joinType = JoinType.INNER_JOIN,
                                    database = TEST_DB_NAME,
                                    table = "other_table",
                                    alias = "table_b",
                                    joinRules = listOf(
                                        JoinRule(
                                            ColumnSpec("table_a", "id"), ColumnSpec("table_b", "id")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    where = listOf(
                        eq(column("table_a", "col_3"), valueOf("cool"))
                    )
                )
            )

            println(SQL)
            println(PARAMS)

            Assert.assertEquals(
                "UPDATE table_a SET table_a.col_1 = table_b.col_1, table_a.col_2 = table_b.col_2 FROM test_sales_db.some_table table_a JOIN test_sales_db.other_table table_b ON ((table_a.id = table_b.id)) WHERE ((table_a.col_3 = ?))",
                SQL.trim()
            )
            Assert.assertEquals(
                listOf("cool"),
                PARAMS
            )
        }
    }

    @Test
    fun mysqlUpdateSyntaxTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();

            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            sqlDB.update(
                SqlUpdateOp(
                    tables = listOf(
                        FromSpec(
                            table = "business", alias = "b",
                            joins = listOf(
                                JoinSpec(
                                    table = "business_geocode", alias = "g",
                                    joinRules = listOf(
                                        JoinRule(ColumnSpec("b", "b_business_id"), ColumnSpec("g", "g_business_id"))
                                    )
                                )
                            )
                        )
                    ),
                    values = listOf(
                        ColumnAndValue(column("b", "mapx"), column("g", "latitude")),
                        ColumnAndValue(column("b", "mapy"), column("g", "longitude"))
                    ),
                    where = listOf(
                        and(
                            or(
                                eq(column("b", "mapx"), valueOf(0)),
                                eq(column("b", "mapx"), valueOf(""))
                            ),
                            gt(column("g", "latitude"), valueOf(0))
                        )
                    )
                )
            )

            println(SQL)
            println(PARAMS.map { "\"$it\"" })

            Assert.assertEquals(
                "UPDATE business b JOIN business_geocode g ON ((b.b_business_id = g.g_business_id)) SET b.mapx = g.latitude, b.mapy = g.longitude WHERE ((((b.mapx = 0) OR (b.mapx = ?)) AND (g.latitude > 0)))",
                SQL.trim()
            )
            Assert.assertEquals(
                listOf(""),
                PARAMS
            )
        }
    }

    @Test
    fun deleteWhereExistsSelectTest() {
        runBlocking {
            var SQL = "";
            var PARAMS: JsonList = listOf();

            class Executor(executor: SqlExecutor) : SqlExecutor by executor {
                override suspend fun execute(sql: String, params: JsonList): UpdateResult {
                    SQL = sql;
                    PARAMS = params;
                    return UpdateResultImpl(0, listOf(), listOf())
                }
            }

            val sqlDB = createSqlDb(Executor(SqlExecutorMock()))

            sqlDB.delete(
                SqlDeleteOp(
                    database = TEST_DB_NAME,
                    table = "Suppliers",
                    where = listOf(
                        exists(
                            joinExpressions(
                                select(listOf(column("ProductName"))),
                                from(listOf(table(TEST_DB_NAME, "Products"))),
                                where(
                                    and(
                                        eq(column("Products", "SupplierID"), column("Suppliers", "supplierID")),
                                        lt(column("Price"), valueOf(20))
                                    )
                                )
                            )
                        )
                    )
                )
            )

            println(SQL)
            println(PARAMS.map { "\"$it\"" })

            Assert.assertEquals(
                "DELETE FROM test_sales_db.Suppliers WHERE (EXISTS (SELECT ProductName FROM test_sales_db.Products WHERE ((Products.SupplierID = Suppliers.supplierID) AND (Price < 20))))",
                SQL.trim()
            )
            Assert.assertEquals(
                listOf<PrimitiveValue>(),
                PARAMS
            )
        }
    }

}

