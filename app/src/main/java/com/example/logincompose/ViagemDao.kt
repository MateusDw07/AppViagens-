package com.example.logincompose

import androidx.room.*
import com.example.logincompose.usuario.Viagem

@Dao
interface ViagemDao {

    @Insert
    suspend fun insert(viagem: Viagem): Long

    @Query("SELECT * FROM viagens WHERE userId = :userId")
    suspend fun listarPorUsuario(userId: Int): List<Viagem>

    @Query("""
    SELECT * FROM viagens
    WHERE LOWER(destino) = LOWER(:cidade)
    AND :dataAtual BETWEEN dataInicio AND dataFim
    AND userId = :userId
    LIMIT 1
""")
    suspend fun buscarViagemAtual(
        cidade: String,
        dataAtual: Long,
        userId: Int
    ): Viagem?
    @Delete
    suspend fun delete(viagem: Viagem)

    @Update
    suspend fun update(viagem: Viagem)
}