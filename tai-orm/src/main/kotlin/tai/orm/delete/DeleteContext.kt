package tai.orm.delete

interface DeleteContext {
    fun add(deleteData: DeleteData): DeleteContext
}
