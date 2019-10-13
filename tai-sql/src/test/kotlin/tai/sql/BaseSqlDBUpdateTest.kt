package tai.sql

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import tai.base.JsonList
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
        }
    }

    @Test
    fun executeSqlOperationDeleteTest() {
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
    }
}

