package tai.sql.impl

import tai.base.JsonMap
import tai.criteria.CriteriaToTextConverter
import tai.sql.CoreSqlDB
import tai.sql.ResultSet
import tai.sql.SqlExecutor
import tai.sql.UpdateResult
import java.util.stream.Stream

class CoreSqlDBImpl(val sqlExecutor: SqlExecutor, val criteriaToTextConverter: CriteriaToTextConverter) : CoreSqlDB {

    override suspend fun query(query: JsonMap): ResultSet {
        val (sql, params) = criteriaToTextConverter.convert(query);
        return sqlExecutor.query(sql, params);
    }

    override suspend fun execute(operation: JsonMap): UpdateResult {
        val (sql, params) = criteriaToTextConverter.convert(operation);
        return sqlExecutor.execute(sql, params);
    }

    override suspend fun executeAll(operations: Stream<JsonMap>): List<UpdateResult> {
        return sqlExecutor.executeAll(
            operations.map { criteriaToTextConverter.convert(it) }
        )
    }
}