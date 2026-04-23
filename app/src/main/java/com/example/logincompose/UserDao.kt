package com.example.logincompose

import androidx.room.*
import com.example.logincompose.usuario.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long
}