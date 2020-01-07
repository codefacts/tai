package tai.orm.update

interface OperationMap {
    fun get(entity: String): OperationHolder
}