package tai.orm.entity.core

import tai.base.JsonMap
import java.lang.NullPointerException

/**
 * Created by Jango on 2017-01-08.
 */
enum class JavaType(val type: Class<*>) {
    OBJECT(Map::class.java), ARRAY(List::class.java), INTEGER(Int::class.java), LONG(Long::class.java), FLOAT(
        Float::class.java
    ),
    DOUBLE(Double::class.java), BOOLEAN(Boolean::class.java), STRING(String::class.java);

    override fun toString(): String {
        return "JavaType{" +
                "aClass=" + type +
                '}'
    }

    companion object {
        fun of(aClass: Class<*>): JavaType {
            return if (aClass == Int::class.java) INTEGER else if (aClass == Long::class.java) LONG else if (aClass == Float::class.java) FLOAT else if (aClass == Double::class.java) DOUBLE else if (aClass == Boolean::class.java) BOOLEAN else if (aClass == String::class.java) STRING
            else throw NullPointerException("Type is not found")
        }
    }

}