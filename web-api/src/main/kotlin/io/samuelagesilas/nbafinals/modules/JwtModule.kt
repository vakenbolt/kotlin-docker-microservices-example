package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.JWT_EXPIRATION_TIME_SECONDS
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.JWT_KEY
import io.samuelagesilas.nbafinals.core.fail
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class JwtModule : AbstractModule() {
    @Provides
    @Singleton
    fun providesJwtAuthenticationHandler(@Named(JWT_KEY) jwtKey: String,
                                         @Named(JWT_EXPIRATION_TIME_SECONDS) expirationTime: Int): JwtAuthentication {
        return JwtAuthentication(jwtKey, expirationTime)
    }
}


class JwtAuthentication @Inject constructor(@Named(JWT_KEY) private val jwtKey: String,
                                            @Named(JWT_EXPIRATION_TIME_SECONDS) private val expirationTime: Int) : Handler<RoutingContext> {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey))!!

    override fun handle(ctx: RoutingContext) {

        val authorizationHeader = ctx.request().getHeader("Authorization")
        if (authorizationHeader == null) {
            ctx.fail(HttpResponseStatus.UNAUTHORIZED)
        } else {
            val token = authorizationHeader.replace("Bearer ", "").trim()
            try {
                val subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
                println(subject)
                ctx.next()
            } catch (e: Exception) {
                ctx.fail(HttpResponseStatus.UNAUTHORIZED)
            }
        }
    }

    fun createJwt(): String {
        val expirationInstant = Instant.now().plus(expirationTime.toLong(), ChronoUnit.SECONDS)
        return Jwts.builder().setSubject("Sam")
                .setExpiration(Date.from(expirationInstant))
                .signWith(secretKey).compact()
    }
}



