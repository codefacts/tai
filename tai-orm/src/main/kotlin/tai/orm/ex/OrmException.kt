package tai.orm.ex

import java.lang.RuntimeException

open class OrmException(message: String): RuntimeException(message) {
}