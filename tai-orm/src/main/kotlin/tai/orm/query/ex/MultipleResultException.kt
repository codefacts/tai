package tai.orm.query.ex

import tai.orm.OrmException

class MultipleResultException(message: String): OrmException(message) {
}