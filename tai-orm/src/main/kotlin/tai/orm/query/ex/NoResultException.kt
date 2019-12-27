package tai.orm.query.ex

import tai.orm.ex.OrmException

class NoResultException(message: String) : OrmException(message) {
}