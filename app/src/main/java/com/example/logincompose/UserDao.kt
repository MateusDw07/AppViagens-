package com.example.logincompose

import androidx.room.*
import com.example.logincompose.usuario.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun login(email: String, senha: String): User?
}
