package com.example.logincompose.telas

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.logincompose.ViewModel.RegisterViewModel
import com.example.logincompose.componentes.PasswordField
import com.example.logincompose.navigation.Routes


@Composable
fun TelasLogin(navController: NavHostController) {
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
        RegisterCard(navController)
    }
}

@Composable
fun RegisterCard(navController: NavHostController) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application)
    )

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
                text = "Novo Usuário",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomTextField(nome, "Nome", "Digite seu nome", Icons.Default.Person) {
                nome = it
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(email, "E-mail", "Digite seu e-mail", Icons.Default.Email) {
                email = it
            }

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(telefone, "Telefone", "Digite seu telefone", Icons.Default.Phone) {
                telefone = it
            }

            Spacer(modifier = Modifier.height(12.dp))

            PasswordField(senha, "Senha", "Crie uma senha") {
                senha = it
            }

            Spacer(modifier = Modifier.height(12.dp))

            PasswordField(confirmarSenha, "Confirmar Senha", "Repita a senha") {
                confirmarSenha = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔴 MOSTRAR ERRO
            erro?.let {
                Text(it, color = Color.Red)
                Spacer(modifier = Modifier.height(10.dp))
            }
            RegisterButton {
                when {
                    nome.isBlank() ||
                            email.isBlank() ||
                            telefone.isBlank() ||
                            senha.isBlank() ||
                            confirmarSenha.isBlank() -> {
                        erro = "Preencha todos os campos"
                    }

                    senha != confirmarSenha -> {
                        erro = "As senhas não coincidem"
                    }

                    else -> {
                        erro = null

                        viewModel.registerUser(
                            nome,
                            email,
                            telefone,
                            senha
                        ) {
                            // ✅ sucesso
                            Toast.makeText(
                                navController.context,
                                "Usuário cadastrado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.Register.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    placeholder: String,
    icon: ImageVector,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(icon, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth()
    )
}



@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text("Registrar", fontSize = 18.sp)
    }
}

