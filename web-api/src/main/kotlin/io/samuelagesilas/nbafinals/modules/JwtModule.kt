package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.core.AUTHENTICATED_JWT_SUBJECT
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.JWT_EXPIRATION_TIME_SECONDS
import io.samuelagesilas.nbafinals.core.ServerConfigPropertyKeys.JWT_KEY
import io.samuelagesilas.nbafinals.core.fail
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import redis.clients.jedis.Jedis
import redis.clients.jedis.params.SetParams
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

data class Jwt(val token: String, val transientUserSubject: String)


class JwtModule : AbstractModule() {
    @Provides
    @Singleton
    fun providesJwtAuthenticationHandler(@Named(JWT_KEY) jwtKey: String,
                                         @Named(JWT_EXPIRATION_TIME_SECONDS) expirationTime: Int,
                                         redis: Jedis): JwtAuthentication {
        return JwtAuthentication(jwtKey, expirationTime, redis)
    }
}


class JwtAuthentication @Inject constructor(@Named(JWT_KEY) private val jwtKey: String,
                                            @Named(JWT_EXPIRATION_TIME_SECONDS) private val expirationTime: Int,
                                            private val redis: Jedis) : Handler<RoutingContext> {

    private val logger = LogManager.getLogger(JwtAuthentication::class.java)

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey))!!

    override fun handle(ctx: RoutingContext) {

        val authorizationHeader = ctx.request().getHeader("Authorization")
        if (authorizationHeader == null) {
            ctx.fail(HttpResponseStatus.UNAUTHORIZED)
        } else {
            try {
                val token = authorizationHeader.replace("Bearer ", "").trim()
                if (!isTokenWhiteListed(token)) throw WhiteListAuthenticationException()
                val jwtSubject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
                ctx.put(AUTHENTICATED_JWT_SUBJECT, jwtSubject).next()
            } catch (e: Exception) {
                logger.error(e)
                ctx.fail(HttpResponseStatus.UNAUTHORIZED)
            }
        }
    }

    fun createJwt(): Jwt {
        val expirationInstant = Instant.now().plus(expirationTime.toLong(), ChronoUnit.SECONDS)
        val transientUserSubject = createTransientUserSubject()
        val jwt = Jwts.builder()
                .setSubject(createTransientUserSubject())
                .setExpiration(Date.from(expirationInstant))
                .signWith(secretKey).compact()
        return Jwt(jwt, transientUserSubject)
    }

    fun whiteListToken(token: String, transientUserSubject: String) {
        redis.use { redis -> redis.set(token, transientUserSubject, SetParams().ex(expirationTime).nx()) }
    }

    fun isTokenWhiteListed(token: String): Boolean {
        return redis.use { redis ->
            redis.get(token)
        }.let { result -> result != null }
    }

    private fun createTransientUserSubject(): String = "user:${Instant.now().toEpochMilli()}"
}

class WhiteListAuthenticationException() : Exception()



