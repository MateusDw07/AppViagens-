package com.example.logincompose.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object Menu : Routes("menu")
    object ForgotPassword : Routes("forgot_password")
}