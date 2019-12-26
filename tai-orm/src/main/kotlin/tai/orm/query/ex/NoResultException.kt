package tai.orm.query.ex

import tai.orm.OrmException

class NoResultException(message: String) : OrmException(message) {
}