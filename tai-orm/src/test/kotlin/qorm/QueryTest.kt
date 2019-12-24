package qorm

import kotlinx.coroutines.runBlocking
import org.junit.Test
import tai.criteria.CriteriaDialectBuilderImpl
import tai.criteria.CriteriaToTextConverterImpl
import tai.criteria.operators.OPERATION_MAP
import tai.criteria.operators.OperationMapImpl
import tai.orm.Pagination
import tai.orm.QueryParam
import tai.orm.core.FieldExpression
import tai.orm.entity.Entity
import test.orm.entity_config.SaleEntities
import tai.orm.entity.EntityUtils
import tai.orm.entity.impl.EntityMappingHelperImpl
import tai.orm.field_
import tai.orm.query.impl.*
import tai.orm.read.makeReadObject
import tai.sql.impl.BaseSqlDBImpl
import tai.sql.impl.CoreSqlDBImpl
import tai.sql.impl.SqlDialectImpl
import tai.sql.impl.SqlExecutorImpl
import test.orm.entities.*
import javax.sql.DataSource

class QueryTest {

    @Test
    fun testWithBasicQuery() {

        val exec = createQueryExecutore(createDateSource(), SaleEntities.entities())

        runBlocking {
            val list = exec.findAll(
                QueryParam(
                    entity = SaleEntities.USER_ENTITY,
                    alias = "r",
                    selections = listOf(
                        FieldExpression.create("r", "id"),
                        FieldExpression.create("r", "username"),
                        FieldExpression.create("r", "password")
                    ),
                    pagination = Pagination(
                        fieldExpression = FieldExpression.create("r", "id"),
                        offset = 25,
                        size = 20
                    )
                )
            )
            println(list)
        }
    }

    @Test
    fun testNestedSelect() {

        val parser = createQueryParser(createDateSource(), SaleEntities.entities())
        val sqlDB = createBaseSqlDB(createDateSource())

        runBlocking {
            val param = QueryParam(
                entity = SaleEntities.FOE_ENTITY,
                alias = "r",
                selections = listOf(
                    FieldExpression.create("r", FoeModel.id),
                    FieldExpression.create("r", FoeModel.createDate),
                    FieldExpression.create("r", FoeModel.updateDate),
                    FieldExpression.create("r", FoeModel.user, UserModel.id),
                    FieldExpression.create("r", FoeModel.user, UserModel.username),
                    FieldExpression.create("r", FoeModel.user, UserModel.password),
                    FieldExpression.create("r", FoeModel.createdBy, UserModel.id),
                    FieldExpression.create("r", FoeModel.createdBy, UserModel.username),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.name),

                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.name),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.dmsCode),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.createDate),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.active),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.name),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.dmsCode),

                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.srs, SRModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.srs, SRModel.createDate),

                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.srs, SRModel.user, UserModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.outlet, OutletModel.srs, SRModel.user, UserModel.username),

                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.user, UserModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.user, UserModel.username),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.outlets, OutletModel.srs, SRModel.user, UserModel.userType),

                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.area, AreaModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.area, AreaModel.name),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.area, AreaModel.region, RegionModel.id),
                    FieldExpression.create("r", FoeModel.territoryMaster, TerritoryMasterModel.area, AreaModel.region, RegionModel.name)
                ),
                pagination = Pagination(
                    fieldExpression = FieldExpression.create("r", "id"),
                    offset = 0,
                    size = 50
                )
            )

            val joinParamMap = createAliasToJoinParamMap(param.joinParams)
            val fullPathExpMap = createAliasToFullPathExpMap(param.alias, param.joinParams, joinParamMap)

            val sqlQry = parser.toSqlQuery(
                param, aliasToJoinParamMap = joinParamMap,
                aliasToFullPathExpMap = fullPathExpMap
            )

            val rs = sqlDB.query(sqlQry)

            val readObject = makeReadObject(
                fieldExpressionToIndexMap = param.selections.asSequence().mapIndexed { index, fieldExpression -> fieldExpression to index }.toMap(),
                rootEntity = param.entity,
                rootAlias = param.alias,
                helper = EntityMappingHelperImpl(EntityUtils.validateAndPreProcess(SaleEntities.entities())),
                aliasToFullPathExpressionMap = fullPathExpMap
            )

            val data = rs.results.map { readObject(it, rs.results) }

            println(data)
        }
    }


}

