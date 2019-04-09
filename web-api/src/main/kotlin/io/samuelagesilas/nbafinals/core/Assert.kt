package io.samuelagesilas.nbafinals.core

fun assert(value: Boolean, failureHandler: () -> Unit) {
    if (value) failureHandler.invoke()
}