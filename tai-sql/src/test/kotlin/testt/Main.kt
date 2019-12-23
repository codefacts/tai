package testt

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.streams.asSequence

fun main() {

    val callback = ::onValueChanged
    d(ThirdParty.TAG, "callback created: $callback")

    val thirdParty = ThirdParty()

    thirdParty.printState()
    thirdParty.addCallback(callback)
    thirdParty.printState()
    thirdParty.removeCallback(callback)
    thirdParty.printState()
}

fun d(s: String, s2: String) {
    println(s + ": " + s2)
    listOf(3, 5).stream().asSequence().asFlow().map { 4 }
}

private fun onValueChanged(value: Int) {
    // do something
}