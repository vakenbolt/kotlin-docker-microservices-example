package io.samuelagesilas.nbafinals.core

inline fun assert(value: Boolean, failureHandler: () -> Unit) {
    if (value) failureHandler.invoke()
}


inline fun <T>assert(block: () -> T, failureHandler: (e: Exception) -> Unit) : T {
    return try {
        block.invoke()
    } catch (e: Exception) {
        failureHandler.invoke(e)
        null!!
    }
}