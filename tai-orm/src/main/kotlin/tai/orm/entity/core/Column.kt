package tai.orm.entity.core

import java.util.*

/**
 * Created by Jango on 2017-01-12.
 */
class Column(name: String, dbType: DbType) {
    val name: String
    val dbType: DbType

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val column = o as Column
        return if (name != column.name) false else dbType === column.dbType
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + dbType.hashCode()
        return result
    }

    override fun toString(): String {
        return "Column{" +
                "name='" + name + '\'' +
                ", dbType=" + dbType +
                '}'
    }

    init {
        Objects.requireNonNull(name)
        Objects.requireNonNull(dbType)
        this.name = name
        this.dbType = dbType
    }
}