package tai.orm.entity.impl

import tai.orm.entity.EntityValidator

/**
 * Created by sohan on 3/17/2017.
 */
class EntityValidatorImpl : EntityValidator {
    override fun validate(params: EntityValidator.Params?) {
        InternalEntityValidator(params!!).validate()
    }

    companion object {
        @JvmStatic
        fun main(afasdf: Array<String>) {
        }
    }
}