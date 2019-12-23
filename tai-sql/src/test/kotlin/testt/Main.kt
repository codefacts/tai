package testt

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
}

private fun onValueChanged(value: Int) {
    // do something
}