package tai.orm.entity.core

import java.util.*

/**
 * Created by Jango on 2017-01-08.
 */
class Entity(
    name: String,
    primaryKey: String,
    fields: Array<Field>,
    dbMapping: DbMapping
) {
    val name: String
    val primaryKey: String
    val fields: Array<Field>
    val dbMapping: DbMapping

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val entity = o as Entity
        return if (name != null) name == entity.name else entity.name == null
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Entity{" +
                "name='" + name + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", fields=" + Arrays.toString(fields) +
                '}'
    }

    init {
        Objects.requireNonNull(name)
        Objects.requireNonNull(primaryKey)
        Objects.requireNonNull(fields)
        Objects.requireNonNull(dbMapping)
        this.name = name
        this.primaryKey = primaryKey
        this.fields = fields
        this.dbMapping = dbMapping
    }
}