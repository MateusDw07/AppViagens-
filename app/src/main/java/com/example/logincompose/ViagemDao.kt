package com.example.logincompose

import androidx.room.*
import com.example.logincompose.usuario.Viagem

@Dao
interface ViagemDao {

    @Insert
    suspend fun insert(viagem: Viagem)

    @Query("SELECT * FROM viagens WHERE userId = :userId")
    suspend fun listarPorUsuario(userId: Int): List<Viagem>

    @Delete
    suspend fun delete(viagem: Viagem)

    @Update
    suspend fun update(viagem: Viagem)
}