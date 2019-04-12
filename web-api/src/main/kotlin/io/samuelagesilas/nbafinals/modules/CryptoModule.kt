package io.samuelagesilas.nbafinals.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.lambdaworks.crypto.SCryptUtil
import javax.inject.Singleton

class CryptoModule: AbstractModule() {

    @Provides
    @Singleton
    fun providesPasswordHasher() : PasswordHasher = SCryptPasswordHasher()
}

interface PasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPasswordHash(password: String, passwordHash: String) : Boolean
}

class SCryptPasswordHasher: PasswordHasher {
    override fun hashPassword(password: String): String = SCryptUtil.scrypt(password, 16384, 8, 1)

    override fun verifyPasswordHash(password: String, passwordHash: String): Boolean = SCryptUtil.check(password,
                                                                                                        passwordHash)

}