package tai.sql

import com.mysql.cj.jdbc.MysqlDataSource
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.sql.impl.BaseSqlDBImpl
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlDBImpl
import tai.sql.impl.MySql5DialectImpl
import java.io.IOException
import java.util.*
import javax.sql.DataSource

fun createDataSource(): DataSource {
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
    val coreSqlDB = createCoreSqlDB(sqlExecutor)
    return SqlDBImpl(
        BaseSqlDBImpl(
            coreSqlDB,
            MySql5DialectImpl(coreSqlDB)
        )
    )
}

fun createCoreSqlDB(sqlExecutor: SqlExecutor): CoreSqlDB {
    return CoreSqlDBImpl(
        sqlExecutor,
        CriteriaToTextConverterImpl(
            OPERATION_MAP,
            CriteriaDialectBuilderImpl()
        )
    )
}
