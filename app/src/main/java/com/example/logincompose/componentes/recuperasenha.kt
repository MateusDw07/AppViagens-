package com.example.logincompose.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.logincompose.navigation.Routes

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E88E5), Color(0xFF64B5F6))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Recuperar Senha",
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    placeholder = { Text("Digite seu e-mail") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔴 ERRO
                erro?.let {
                    Text(it, color = Color.Red)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Button(
                    onClick = {
                        when {
                            email.isBlank() -> {
                                erro = "Digite seu e-mail"
                            }

                            !email.contains("@") -> {
                                erro = "E-mail inválido"
                            }

                            else -> {
                                erro = null

                                // ✅ VOLTA PRO LOGIN
                                navController.navigate(Routes.Login.route) {
                                    popUpTo(Routes.ForgotPassword.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enviar", fontSize = 18.sp)
                }
            }
        }
    }
}