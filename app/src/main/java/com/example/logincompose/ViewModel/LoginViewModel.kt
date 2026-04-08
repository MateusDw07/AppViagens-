package com.example.logincompose.ViewModel

import androidx.lifecycle.ViewModel
import com.example.logincompose.loginViewModel.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onSenhaChange(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha)
    }

    fun login() {
        val state = _uiState.value

        if (state.email.isBlank() || state.senha.isBlank()) {
            _uiState.value = state.copy(
                error = "Preencha todos os campos"
            )
        } else {
            _uiState.value = state.copy(error = null)
        }
    }
}