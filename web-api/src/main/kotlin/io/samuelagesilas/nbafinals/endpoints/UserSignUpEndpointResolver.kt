package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.*
import io.samuelagesilas.nbafinals.core.ApiExceptionFactory
import io.samuelagesilas.nbafinals.core.Keys
import io.samuelagesilas.nbafinals.core.ResolverResponse
import io.samuelagesilas.nbafinals.core.check
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.modules.JwtAuthentication
import org.apache.logging.log4j.LogManager
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*
import javax.inject.Inject
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserSignUpRequest(@get: NotNull
                             @get: NotEmpty
                             @get: Size(min = 8, max = 100)
                             val username: String,

                             @get: NotNull
                             @get: NotEmpty
                             @get: Size(min = 8, max = 100)
                             val password: String)

class UserSignUpEndpointResolver @Inject constructor(private val jwtAuthentication: JwtAuthentication,
                                                     private val usersDao: UsersDAO,
                                                     private val apiException: ApiExceptionFactory) {

    val logger = LogManager.getLogger(UserSignUpEndpointResolver::class.java)

    fun signUpUser(username: String, passwordHash: String, locale: Locale): ResolverResponse<AuthenticationResponse> {
        var result = 0
        try {
            result = usersDao.insertUser(username, passwordHash)
        } catch (e: UnableToExecuteStatementException) {
            when (e.cause != null) {
                (e.cause is SQLIntegrityConstraintViolationException) -> {
                    logger.error(e)
                    throw apiException.create(CONFLICT, locale, Keys.USERNAME_NOT_AVAILABLE)
                }
            }
            logger.error(e)
        }
        check(result < 1) { throw apiException.create(NOT_FOUND, locale, Keys.NO_RECORDS_FOUND) }
        val jwt = jwtAuthentication.createJwt()
        jwtAuthentication.whiteListToken(jwt.token, jwt.transientUserSubject)
        return ResolverResponse(AuthenticationResponse(bearer = jwt.token), status = CREATED)
    }
}