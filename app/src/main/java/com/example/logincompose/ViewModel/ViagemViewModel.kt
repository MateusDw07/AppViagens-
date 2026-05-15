package com.example.logincompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    private val _viagens =
        MutableStateFlow<List<Viagem>>(emptyList())

    val viagens: StateFlow<List<Viagem>> =
        _viagens

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

    fun carregarViagens(userId: Int) {

        viewModelScope.launch {

            _viagens.value =
                repository.listarPorUsuario(userId)
        }
    }

    fun deletarViagem(
        viagem: Viagem,
        userId: Int
    ) {

        viewModelScope.launch {

            repository.deletarViagem(viagem)

            carregarViagens(userId)
        }
    }
}

class ViagemViewModelFactory(
    private val repository: ViagemRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ViagemViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ViagemViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}