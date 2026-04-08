package com.example.logincompose.loginViewModel

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val error: String? = null
)