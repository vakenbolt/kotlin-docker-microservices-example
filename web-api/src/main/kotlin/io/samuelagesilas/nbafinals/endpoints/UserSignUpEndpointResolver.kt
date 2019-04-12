package io.samuelagesilas.nbafinals.endpoints

import io.netty.handler.codec.http.HttpResponseStatus.*
import io.samuelagesilas.nbafinals.core.*
import io.samuelagesilas.nbafinals.dao.User
import io.samuelagesilas.nbafinals.dao.UsersDAO
import io.samuelagesilas.nbafinals.modules.AuthenticationHandler
import io.samuelagesilas.nbafinals.modules.PasswordHasher
import org.apache.logging.log4j.LogManager
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import java.sql.SQLException
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

class UserSignUpEndpointResolver @Inject constructor(private val usersDao: UsersDAO,
                                                     private val hasher: PasswordHasher,
                                                     private val jwtAuthentication: AuthenticationHandler,
                                                     private val apiException: ApiExceptionFactory) {

    val logger = LogManager.getLogger(UserSignUpEndpointResolver::class.java)

    @Throws(ApiException::class)
    fun signUpUser(username: String, password: String, locale: Locale): ResolverResponse<AuthenticationResponse> {
        var updateCount = 0
        var user: User? = null
        try {
            //TODO: ojo -> This should be in a transaction
            updateCount = usersDao.insertUser(username, hasher.hashPassword(password))
            user = usersDao.selectUserByUsername(username)

        } catch (e: SQLException) {
            when (e.cause != null) {
                (e.cause is SQLIntegrityConstraintViolationException) -> {
                    logger.error(e)
                    throw apiException.create(CONFLICT, locale, Keys.USERNAME_NOT_AVAILABLE)
                }
            }
            logger.error(e)
        }
        check(updateCount < 1) { throw IllegalStateException("Error: A database update was expected but not found. ") }
        if (user == null) {
            throw apiException.create(INTERNAL_SERVER_ERROR)
        }
        val jwt = jwtAuthentication.createJwt(user.id)
        jwtAuthentication.whiteListToken(jwt)
        return ResolverResponse(AuthenticationResponse(bearer = jwt.token), status = CREATED)
    }
}