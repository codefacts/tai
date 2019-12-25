package tai.sql

import org.junit.Test
import tai.sql.impl.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import tai.base.JsonList
import tai.criteria.operators.Order
import tai.criteria.ops.*

class SqlDBTest {

    @Test
    fun queryTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("username"),
                        column("password")
                    ),
                    from = listOf(
                        FromSpec(table = "users")
                    ),
                    where = listOf(
                        eq(column("user_type"), valueOf("foe")),
                        eq(column("active"), valueOf(true))
                    ),
                    groupBy = listOf(
                        ColumnSpec("username"),
                        ColumnSpec("password")
                    ),
                    orderBy = listOf(
                        OrderBySpec("username", Order.ASC),
                        OrderBySpec("password", Order.DESC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT username, password FROM users WHERE ((user_type = ?) AND (active = true)) GROUP BY username, password ORDER BY username ASC, password DESC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf("foe"),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun queryTest2() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("username"),
                        column("password")
                    ),
                    from = listOf(
                        FromSpec(table = "users")
                    ),
                    where = listOf(
                        eq(column("user_type"), valueOf("foe"))
                    ),
                    groupBy = listOf(
                        ColumnSpec("username")
                    ),
                    orderBy = listOf(
                        OrderBySpec("username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT username, password FROM users WHERE ((user_type = ?)) GROUP BY username ORDER BY username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf("foe"),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun queryWithEmptyWhereTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("username"),
                        column("password")
                    ),
                    from = listOf(
                        FromSpec(table = "users")
                    ),
                    where = listOf(),
                    groupBy = listOf(
                        ColumnSpec("username")
                    ),
                    orderBy = listOf(
                        OrderBySpec("username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT username, password FROM users GROUP BY username ORDER BY username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf<String>(),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun queryWithEmptyWhereNoGroupByTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("username"),
                        column("password")
                    ),
                    from = listOf(
                        FromSpec(table = "users")
                    ),
                    where = listOf(),
                    orderBy = listOf(
                        OrderBySpec("username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT username, password FROM users ORDER BY username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf<String>(),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun queryWithJoinsWithoutAliasTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        star()
                    ),
                    from = listOf(
                        FromSpec(
                            table = "foes",
                            joins = listOf(
                                JoinSpec(
                                    table = "users",
                                    joinRules = listOf(
                                        JoinRule(
                                            from = ColumnSpec("user_id"),
                                            to = ColumnSpec("id")
                                        ),
                                        JoinRule(
                                            from = ColumnSpec("user_id"),
                                            to = ColumnSpec("id")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    where = listOf(),
                    orderBy = listOf(
                        OrderBySpec("username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT * FROM foes JOIN users ON ((user_id = id) AND (user_id = id)) ORDER BY username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf<String>(),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun queryWithJoinsTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("s", "sec_type_id"),
                        column("f", "territory_master_id"),
                        column("u1", "username"),
                        column("u2", "username")
                    ),
                    from = listOf(
                        FromSpec(
                            table = "srs",
                            alias = "s",
                            joins = listOf(
                                JoinSpec(
                                    table = "users",
                                    alias = "u1",
                                    joinRules = listOf(
                                        JoinRule(
                                            from = ColumnSpec("s", "user_id"),
                                            to = ColumnSpec("u1", "id")
                                        ),
                                        JoinRule(
                                            from = ColumnSpec("s", "user_id"),
                                            to = ColumnSpec("u1", "id")
                                        )
                                    )
                                )
                            )
                        ),
                        FromSpec(
                            table = "foes",
                            alias = "f",
                            joins = listOf(
                                JoinSpec(
                                    table = "users",
                                    alias = "u2",
                                    joinRules = listOf(
                                        JoinRule(
                                            from = ColumnSpec("f", "user_id"),
                                            to = ColumnSpec("u2", "id")
                                        ),
                                        JoinRule(
                                            from = ColumnSpec("f", "user_id"),
                                            to = ColumnSpec("u2", "id")
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    where = listOf(),
                    groupBy = listOf(
                        ColumnSpec("u1", "username"),
                        ColumnSpec("u2", "username")
                    ),
                    orderBy = listOf(
                        OrderBySpec("u1", "username", Order.ASC),
                        OrderBySpec("u2", "username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT s.sec_type_id, f.territory_master_id, u1.username, u2.username FROM srs s JOIN users u1 ON ((s.user_id = u1.id) AND (s.user_id = u1.id)), foes f JOIN users u2 ON ((f.user_id = u2.id) AND (f.user_id = u2.id)) GROUP BY u1.username, u2.username ORDER BY u1.username ASC, u2.username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf<String>(),
                testSqlExecutoer.params
            )
        }
    }

    @Test
    fun havingTest() {
        runBlocking {
            val testSqlExecutoer = ExecutorMock(SqlExecutorMock())
            val db = createSqlDb(testSqlExecutoer);

            val resultSet = db.query(
                SqlQuery(
                    selections = listOf(
                        column("u", "username")
                    ),
                    from = listOf(
                        FromSpec(
                            table = "users",
                            alias = "u"
                        )
                    ),
                    where = listOf(),
                    groupBy = listOf(
                        ColumnSpec("u", "username")
                    ),
                    having = listOf(
                        gte(
                            sum(column("u", "salary")), valueOf(150000)
                        ),
                        gte(
                            sum(column("u", "income")), valueOf(500)
                        )
                    ),
                    orderBy = listOf(
                        OrderBySpec("u", "username", Order.ASC)
                    )
                )
            )
            println(testSqlExecutoer.sql);
            println(testSqlExecutoer.params)
            Assert.assertEquals(
                "SELECT u.username FROM users u GROUP BY u.username HAVING ((SUM(u.salary) >= 150000) AND (SUM(u.income) >= 500)) ORDER BY u.username ASC",
                testSqlExecutoer.sql.trim()
            )
            Assert.assertEquals(
                listOf<String>(),
                testSqlExecutoer.params
            )
        }
    }
}

class ExecutorMock(executor: SqlExecutor) : SqlExecutor by executor {
    var sql = "";
    var params: JsonList = listOf();
    override suspend fun query(sql: String, params: JsonList): ResultSet {
        this.sql = sql;
        this.params = params;
        return ResultSetImpl(listOf(), listOf());
    }
}