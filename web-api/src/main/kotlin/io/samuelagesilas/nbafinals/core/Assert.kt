package io.samuelagesilas.nbafinals.core

fun assert(value: Boolean, failureHandler: () -> Unit) {
    if (value) failureHandler.invoke()
}


fun <T>assert(block: () -> T, failureHandler: (e: Exception) -> Unit) : T {
    return try {
        block.invoke()
    } catch (e: Exception) {
        failureHandler.invoke(e)
        null!!
    }
}