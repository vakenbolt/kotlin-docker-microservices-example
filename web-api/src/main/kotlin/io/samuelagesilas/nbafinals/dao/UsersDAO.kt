package io.samuelagesilas.nbafinals.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface UsersDAO {
    @SqlUpdate("INSERT INTO users (username, password_hash) VALUES (:username, :password_hash)")
    fun insertUser(@Bind("username") username: String, @Bind("password_hash") password: String) : Int

    @SqlQuery("SELECT password_hash from users where username=:username")
    fun authenticateUser(@Bind("username") username: String) : String?
}