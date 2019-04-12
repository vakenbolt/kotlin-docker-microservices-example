package io.samuelagesilas.nbafinals.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.SQLException

interface UsersDAO {
    @SqlUpdate("INSERT INTO users (username, password_hash) VALUES (:username, :password_hash)")
    @Throws(SQLException::class)
    fun insertUser(@Bind("username") username: String, @Bind("password_hash") password: String) : Int

    @SqlQuery("SELECT id, password_hash from users where username=:username")
    fun selectUserByUsername(@Bind("username") username: String) : User?
}

data class User(val id: Long, val passwordHash: String)