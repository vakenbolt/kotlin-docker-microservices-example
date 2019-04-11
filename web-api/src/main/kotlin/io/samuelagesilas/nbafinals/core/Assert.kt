package io.samuelagesilas.nbafinals.core

inline fun check(value: Boolean, failureHandler: () -> Unit) {
    if (value) failureHandler.invoke()
}


inline fun <T>check(block: () -> T, failureHandler: (e: Exception) -> Unit) : T {
    return try {
        block.invoke()
    } catch (e: Exception) {
        failureHandler.invoke(e)
        null!!
    }
}