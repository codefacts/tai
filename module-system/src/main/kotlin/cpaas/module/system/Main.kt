package cpaas.module.system

/**
 * Created by Jango on 9/12/2016.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val moduleSystem = ModuleSystem.builder()
            .export(
                String::class.java,
                "1",
                { "module 1" }
            )
            .export(
                String::class.java,
                { "ff" }
            )
            .export(
                Int::class.java,
                { 1 }
            )
            .export(
                Boolean::class.java,
                {
                    println(require(Int::class.java))
                    println(require(String::class.java))
                    println(require(String::class.java, "1"))
                    return@export true
                }
            )
            .build()
        val require = moduleSystem.require(Boolean::class.java)
        println(require)
    }
}