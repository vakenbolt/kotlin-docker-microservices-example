package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.netty.handler.codec.http.HttpResponseStatus
import io.samuelagesilas.nbafinals.core.AUTHENTICATED_JWT_SUBJECT
import io.samuelagesilas.nbafinals.core.fail
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import redis.clients.jedis.Jedis
import redis.clients.jedis.params.SetParams
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

data class Jwt(val token: String, val userId: Long)


class JwtModule : AbstractModule() {
    @Provides
    @Singleton
    fun providesJwtAuthenticationHandler(serverConfig: ServerConfig, redis: Jedis): AuthenticationHandler {
        return JwtAuthentication(serverConfig, redis)
    }
}


interface AuthenticationHandler : Handler<RoutingContext> {
    val logger: Logger?
    val secretKey: SecretKey
    fun createJwt(userId: Long): Jwt
    fun whiteListToken(jwt: Jwt)
    fun isTokenWhiteListed(jwtSubject: String, token: String): Boolean
}

class JwtAuthentication @Inject constructor(private val serverConfig: ServerConfig,
                                            private val redis: Jedis) : AuthenticationHandler {

    override val logger = LogManager.getLogger(JwtAuthentication::class.java)

    override val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(serverConfig.jwtKey))!!

    override fun handle(ctx: RoutingContext) {

        val authorizationHeader = ctx.request().getHeader("Authorization")
        if (authorizationHeader == null) {
            ctx.fail(HttpResponseStatus.UNAUTHORIZED)
        } else {
            try {
                val token = authorizationHeader.replace("Bearer ", "").trim()
                val jwtSubject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
                if (!isTokenWhiteListed(jwtSubject, token)) throw WhiteListAuthenticationException()
                ctx.put(AUTHENTICATED_JWT_SUBJECT, jwtSubject).next()
            } catch (e: Exception) {
                logger.error(e)
                ctx.fail(HttpResponseStatus.UNAUTHORIZED)
            }
        }
    }

    override fun createJwt(userId: Long): Jwt {
        val expirationInstant = Instant.now().plus(serverConfig.jwt_expiration_time_seconds.toLong(), ChronoUnit.SECONDS)
        val jwt = Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(Date.from(expirationInstant))
                .signWith(secretKey).compact()
        return Jwt(jwt, userId)
    }

    override fun whiteListToken(jwt: Jwt) {
        redis.use { redis ->
            val key = "user:${jwt.userId}"
            redis.set(key, jwt.token, SetParams().ex(serverConfig.jwt_expiration_time_seconds).nx())
        }
    }

    override fun isTokenWhiteListed(jwtSubject: String, token: String): Boolean {
        return redis.use { redis ->
            with (redis.get("user:${jwtSubject.toLong()}")) { !(this.isNullOrBlank() || this != token) }
        }
    }
}

class WhiteListAuthenticationException() : Exception()



