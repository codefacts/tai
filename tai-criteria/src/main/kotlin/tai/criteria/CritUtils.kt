package tai.criteria

interface ParamsBuilder {
    fun add(param: Any);
    fun build(): List<Any>;
}

class ParamsBuilderImpl(private val list: MutableList<Any>) : ParamsBuilder {
    override fun add(param: Any) {
        list.add(param);
    }

    override fun build(): List<Any> {
        return list.toList();
    }
}
