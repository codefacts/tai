package tai.sql

import com.mysql.cj.jdbc.MysqlDataSource
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