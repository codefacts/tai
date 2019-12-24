package qorm

import com.mysql.cj.jdbc.MysqlDataSource
import com.mysql.cj.util.TestUtils
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.operators.OperationMapImpl
import tai.orm.entity.Entity
import tai.orm.entity.EntityUtils
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.field_
import tai.orm.query.impl.FieldExpOperator
import tai.orm.query.impl.QueryExecutorImpl
import tai.orm.query.impl.QueryParser
import tai.sql.SqlDB
import tai.sql.SqlExecutor
import tai.sql.impl.*
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

fun createQueryExecutore(createDateSource: DataSource, ormEntities: Collection<Entity>): QueryExecutorImpl {
    val opMap = OperationMapImpl(
        OPERATION_MAP.operationMap + mapOf(
            field_ to FieldExpOperator()
        )
    )

    val entities = EntityUtils.validateAndPreProcess(ormEntities)
    val helper = EntityMappingHelperImpl(entities)
    val baseSqlDB = BaseSqlDBImpl(
        CoreSqlDBImpl(
            SqlExecutorImpl(createDateSource()),
            CriteriaToTextConverterImpl(
                opMap, CriteriaDialectBuilderImpl()
            )
        ), SqlDialectImpl()
    )
    return QueryExecutorImpl(helper, baseSqlDB)
}

fun createQueryParser(createDateSource: DataSource, ormEntities: Collection<Entity>): QueryParser {
    val opMap = OperationMapImpl(
        OPERATION_MAP.operationMap + mapOf(
            field_ to FieldExpOperator()
        )
    )

    val entities = EntityUtils.validateAndPreProcess(ormEntities)
    val helper = EntityMappingHelperImpl(entities)
    val baseSqlDB = BaseSqlDBImpl(
        CoreSqlDBImpl(
            SqlExecutorImpl(createDateSource()),
            CriteriaToTextConverterImpl(
                opMap, CriteriaDialectBuilderImpl()
            )
        ), SqlDialectImpl()
    )
    return QueryParser(helper)
}

fun createBaseSqlDB(createDateSource: DataSource): BaseSqlDBImpl {

    val opMap = OperationMapImpl(
        OPERATION_MAP.operationMap + mapOf(
            field_ to FieldExpOperator()
        )
    )

    return BaseSqlDBImpl(
        CoreSqlDBImpl(
            SqlExecutorImpl(createDateSource),
            CriteriaToTextConverterImpl(opMap, CriteriaDialectBuilderImpl())
        ),
        SqlDialectImpl()
    )
}

fun createDateSource(): DataSource {
    return getMySQLDataSource()
}