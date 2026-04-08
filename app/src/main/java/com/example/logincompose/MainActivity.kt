package com.example.logincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.logincompose.ViewModel.LoginViewModel
import com.example.logincompose.loginViewModel.LoginUiState
import com.example.logincompose.navigation.AppNavigation
import com.example.logincompose.navigation.Routes



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoginCard(
            state = state,
            onEmailChange = viewModel::onEmailChange,
            onSenhaChange = viewModel::onSenhaChange,

            onLoginClick = {
                viewModel.login()
            },

            onNovoUsuarioClick = {
                navController.navigate(Routes.Register.route)
            },

            onForgotPasswordClick = {
                navController.navigate(Routes.ForgotPassword.route)
            }
        )
    }

    // 🚨 navegação segura (IMPORTANTE)
    LaunchedEffect(state.error) {
        if (state.error == null &&
            state.email.isNotBlank() &&
            state.senha.isNotBlank()
        ) {
            navController.navigate(Routes.Menu.route) {
                popUpTo(Routes.Login.route) { inclusive = true }
            }
        }
    }
}

@Composable
fun LoginCard(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNovoUsuarioClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    Card {
        Column(modifier = Modifier.padding(20.dp)) {

            EmailField(state.email, onEmailChange)

            PasswordField(state.senha, onSenhaChange)

            LoginButton(onLoginClick)

            state.error?.let {
                Text(it, color = Color.Red)
            }

            FooterLinks(
                onNovoUsuarioClick,
                onForgotPasswordClick
            )
        }
    }
}

@Composable
fun EmailField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text("E-mail") },
        placeholder = { Text("Digite seu e-mail") },
        leadingIcon = {
            Icon(Icons.Default.Email, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordField(value: String, onChange: (String) -> Unit) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text("Senha") },
        placeholder = { Text("Digite sua senha") },
        leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text("Entrar", fontSize = 18.sp)
    }
}

@Composable
fun FooterLinks(
    onNovoUsuarioClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Novo Usuário",
            modifier = Modifier.clickable {
                onNovoUsuarioClick()
            }
        )

        Text(
            text = "Esqueci a Senha",
            modifier = Modifier.clickable {
                onForgotPasswordClick()
            }
        )
    }
}




