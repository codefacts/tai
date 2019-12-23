package qorm

import com.mysql.cj.jdbc.MysqlDataSource
import com.mysql.cj.util.TestUtils
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.sql.SqlDB
import tai.sql.SqlExecutor
import tai.sql.impl.BaseSqlDBImpl
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlDBImpl
import tai.sql.impl.SqlDialectImpl
import java.io.IOException
import java.util.*
import javax.sql.DataSource

fun getMySQLDataSource(): DataSource {
    val props = Properties()
    var mysqlDS = MysqlDataSource()
    try {
        val fis = TestUtils::class.java.getResourceAsStream("../../../../db.properties")
        props.load(fis)
        mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"))
        mysqlDS.user = props.getProperty("MYSQL_DB_USERNAME")
        mysqlDS.password = props.getProperty("MYSQL_DB_PASSWORD")
        println()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return mysqlDS
}

fun createSqlDb(sqlExecutor: SqlExecutor): SqlDB {
    return SqlDBImpl(
        BaseSqlDBImpl(
            CoreSqlDBImpl(
                sqlExecutor,
                CriteriaToTextConverterImpl(
                    OPERATION_MAP,
                    CriteriaDialectBuilderImpl()
                )
            ),
            SqlDialectImpl()
        )
    )
}