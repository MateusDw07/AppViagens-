package com.example.logincompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.logincompose.LoginScreen
import com.example.logincompose.componentes.ForgotPasswordScreen
import com.example.logincompose.telas.MenuScreen
import com.example.logincompose.telas.TelasLogin

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route // ✅ CORRETO
    ) {

        composable(Routes.Login.route) { // ✅ CORRETO
            LoginScreen(navController)
        }

        composable(Routes.Register.route) {
            TelasLogin(navController)
        }

        composable(Routes.Menu.route) {
            MenuScreen()
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }
    }
}