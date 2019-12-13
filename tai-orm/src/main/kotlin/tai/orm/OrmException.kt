package tai.orm

import java.lang.RuntimeException

open class OrmException(message: String): RuntimeException(message) {
}