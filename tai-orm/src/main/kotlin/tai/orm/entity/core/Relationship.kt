package tai.orm.entity.core

import java.util.*

/**
 * Created by Jango on 2017-01-12.
 */
class Relationship(
    type: Type,
    name: Name,
    entity: String
) {
    val type: Type
    val name: Name
    val entity: String

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Relationship
        if (type != that.type) return false
        if (name != that.name) return false
        return if (entity != null) entity == that.entity else that.entity == null
    }

    override fun hashCode(): Int {
        var result = type.hashCode() ?: 0
        result = 31 * result + (name.hashCode() ?: 0)
        result = 31 * result + (entity.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Relationship{" +
                "type=" + type +
                ", name=" + name +
                ", entity='" + entity + '\'' +
                '}'
    }

    enum class Name {
        HAS_ONE, HAS_MANY
    }

    enum class Type {
        ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY
    }

    init {
        Objects.requireNonNull(type)
        Objects.requireNonNull(name)
        Objects.requireNonNull(entity)
        this.type = type
        this.name = name
        this.entity = entity
    }
}