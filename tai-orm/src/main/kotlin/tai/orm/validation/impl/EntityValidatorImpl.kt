package tai.orm.validation.impl

import tai.orm.validation.EntityValidator

/**
 * Created by sohan on 3/17/2017.
 */
class EntityValidatorImpl : EntityValidator {

    override fun validate(params: EntityValidator.Params) {
        InternalEntityValidator(params).validate()
    }
}