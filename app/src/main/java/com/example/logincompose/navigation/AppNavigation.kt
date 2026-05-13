package com.example.logincompose.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.logincompose.LoginScreen
import com.example.logincompose.componentes.ForgotPasswordScreen
import com.example.logincompose.telas.MenuScreen
import com.example.logincompose.telas.MinhasViagensScreen
import com.example.logincompose.telas.NovaViagemScreen
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
            MenuScreen(navController)
        }

        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }

        composable("nova_viagem") {
            NovaViagemScreen(userId = 1)
        }

        composable("minhas_viagens") {
            MinhasViagensScreen(userId = 1)
        }

        composable(Routes.Sobre.route) {
            Text("Sobre o app")
        }
    }
}