package tai.orm.delete.impl

import tai.orm.delete.DeleteContext
import tai.orm.delete.DeleteData
import java.util.*

/**
 * Created by Jango on 17/02/07.
 */
class DeleteContextImpl(val deleteDataSet: MutableSet<DeleteData>) : DeleteContext {

    override fun add(deleteData: DeleteData): DeleteContext {
        if (deleteDataSet.contains(deleteData)) {
            return this
        }
        deleteDataSet.add(deleteData)
        return this
    }
}