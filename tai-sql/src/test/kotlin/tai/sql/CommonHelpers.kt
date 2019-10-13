package tai.sql

import com.mysql.cj.jdbc.MysqlDataSource
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.operationMap
import tai.sql.impl.BaseSqlDBImpl
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlDBImpl
import tai.sql.impl.SqlDialectImpl
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.sql.DataSource

fun getMySQLDataSource(): DataSource {
    val props = Properties()
    var mysqlDS = MysqlDataSource()
    try {
        val fis = SqlDBTest::class.java.getResourceAsStream("../../db.properties")
        props.load(fis)
        mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"))
        mysqlDS.user = props.getProperty("MYSQL_DB_USERNAME")
        mysqlDS.password = props.getProperty("MYSQL_DB_PASSWORD")
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
                    operationMap,
                    CriteriaDialectBuilderImpl()
                )
            ),
            SqlDialectImpl()
        )
    )
}