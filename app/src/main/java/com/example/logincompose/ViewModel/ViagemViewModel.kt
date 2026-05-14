package com.example.logincompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logincompose.repository.ViagemRepository
import com.example.logincompose.usuario.Viagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViagemViewModel(
    private val repository: ViagemRepository
) : ViewModel() {

    private val _viagemAtual =
        MutableStateFlow<Viagem?>(null)

    val viagemAtual: StateFlow<Viagem?> =
        _viagemAtual

    fun buscarViagemAtual(
        cidade: String,
        userId: Int
    ) {

        viewModelScope.launch {

            val viagem =
                repository.buscarViagemAtual(
                    cidade,
                    System.currentTimeMillis(),
                    userId
                )

            _viagemAtual.value = viagem
        }
    }
}