package tai.orm.delete

import tai.base.JsonMap

@FunctionalInterface
interface DeleteFunction {
    fun delete(entity: JsonMap, deleteContext: DeleteContext): JsonMap
}