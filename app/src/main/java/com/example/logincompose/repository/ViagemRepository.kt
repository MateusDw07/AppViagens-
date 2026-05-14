package com.example.logincompose.repository

import com.example.logincompose.ViagemDao
import com.example.logincompose.usuario.Viagem

class ViagemRepository(
    private val viagemDao: ViagemDao
) {

    suspend fun buscarViagemAtual(
        cidade: String,
        dataAtual: Long,
        userId: Int
    ): Viagem? {

        return viagemDao.buscarViagemAtual(
            cidade,
            dataAtual,
            userId
        )
    }
}