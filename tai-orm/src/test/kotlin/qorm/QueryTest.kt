package qorm

import org.junit.Test
import test.orm.entity_config.Entities
import tai.orm.entity.EntityUtils

class QueryTest {

    @Test
    fun testToSqlQuery() {
        val entities = EntityUtils.validateAndPreProcess(Entities.entities())
        println("Entities validated: ${entities.size}")
    }
}