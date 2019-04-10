package io.samuelagesilas.nbafinals

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*

fun main() {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    println(Base64.getEncoder().encodeToString(key.encoded))
}