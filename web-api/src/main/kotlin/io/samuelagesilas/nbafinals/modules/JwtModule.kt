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
import redis.clients.jedis.Jedis
import redis.clients.jedis.params.SetParams
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

data class Jwt(val token: String, val userId: Long)


class JwtModule : AbstractModule() {
    @Provides
    @Singleton
    fun providesJwtAuthenticationHandler(serverConfig: ServerConfig, redis: Jedis): JwtAuthentication {
        return JwtAuthentication(serverConfig, redis)
    }
}


class JwtAuthentication @Inject constructor(private val serverConfig: ServerConfig,
                                            private val redis: Jedis) : Handler<RoutingContext> {

    private val logger = LogManager.getLogger(JwtAuthentication::class.java)

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(serverConfig.jwtKey))!!

    override fun handle(ctx: RoutingContext) {

        val authorizationHeader = ctx.request().getHeader("Authorization")
        if (authorizationHeader == null) {
            ctx.fail(HttpResponseStatus.UNAUTHORIZED)
        } else {
            try {
                val token = authorizationHeader.replace("Bearer ", "").trim()
                val jwtSubject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
                if (!isTokenWhiteListed(jwtSubject)) throw WhiteListAuthenticationException()
                ctx.put(AUTHENTICATED_JWT_SUBJECT, jwtSubject).next()
            } catch (e: Exception) {
                logger.error(e)
                ctx.fail(HttpResponseStatus.UNAUTHORIZED)
            }
        }
    }

    fun createJwt(userId: Long): Jwt {
        val expirationInstant = Instant.now().plus(serverConfig.jwt_expiration_time_seconds.toLong(), ChronoUnit.SECONDS)
        val jwt = Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(Date.from(expirationInstant))
                .signWith(secretKey).compact()
        return Jwt(jwt, userId)
    }

    fun whiteListToken(jwt: Jwt) {
        redis.use { redis ->
            val key: String = "user:${jwt.userId}"
            redis.set(key, jwt.token, SetParams().ex(serverConfig.jwt_expiration_time_seconds).nx())
        }
    }

    fun isTokenWhiteListed(jwtSubject: String): Boolean {
        return redis.use { redis ->
            redis.get("user:${jwtSubject.toLong()}")
        }.let { result -> result != null }
    }
}

class WhiteListAuthenticationException() : Exception()



