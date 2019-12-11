package tai.orm.entity.core

import java.util.*

/**
 * Created by Jango on 2017-01-12.
 */
class ForeignColumnMapping(srcColumn: String, dstColumn: String) {
    val srcColumn: String
    val dstColumn: String

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ForeignColumnMapping
        return if (srcColumn != that.srcColumn) false else dstColumn == that.dstColumn
    }

    override fun hashCode(): Int {
        var result = srcColumn.hashCode()
        result = 31 * result + dstColumn.hashCode()
        return result
    }

    override fun toString(): String {
        return "ForeignColumnMapping{" +
                "srcColumn='" + srcColumn + '\'' +
                ", dstColumn='" + dstColumn + '\'' +
                '}'
    }

    init {
        Objects.requireNonNull(srcColumn)
        Objects.requireNonNull(dstColumn)
        this.srcColumn = srcColumn
        this.dstColumn = dstColumn
    }
}