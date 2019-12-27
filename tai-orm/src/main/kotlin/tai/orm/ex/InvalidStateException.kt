package tai.orm.ex

import tai.orm.ex.OrmException

class InvalidStateException(message: String): OrmException(message) {
}