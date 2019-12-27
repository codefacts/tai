package tai.orm.query.ex

import tai.orm.ex.OrmException

class MultipleResultException(message: String): OrmException(message) {
}