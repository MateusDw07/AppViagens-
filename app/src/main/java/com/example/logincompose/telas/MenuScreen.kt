package com.example.logincompose.telas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.logincompose.navigation.Routes
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.example.logincompose.AppDatabase
import com.example.logincompose.location.LocationHelper
import com.example.logincompose.repository.ViagemRepository
import com.example.logincompose.viewmodel.ViagemViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController) {
    val context = navController.context

    val db = AppDatabase.getDatabase(context)

    val repository = ViagemRepository(
        db.viagemDao()
    )

    val viewModel = remember {
        ViagemViewModel(repository)
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val locationHelper = LocationHelper(context)

    val viagemAtual by viewModel
        .viagemAtual
        .collectAsState()

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                locationHelper.getCidade { cidade ->

                    if (cidade != null) {

                        viewModel.buscarViagemAtual(
                            cidade,
                            1
                        )
                    }
                }
            }
        }

    LaunchedEffect(Unit) {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationHelper.getCidade { cidade ->

                if (cidade != null) {

                    viewModel.buscarViagemAtual(
                        cidade,
                        1
                    )
                }
            }

        } else {

            launcher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    // 🔴 BOTÃO VOLTAR FECHA O APP
    BackHandler {
        (navController.context as? android.app.Activity)?.finish()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController)
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Menu") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->

            // 👇 SUA TELA ORIGINAL FICA AQUI
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column {

                    Text("Menu 🎉")

                    Spacer(modifier = Modifier.height(24.dp))

                    viagemAtual?.let { viagem ->

                        Card {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "Viagem Atual",
                                    style =
                                        MaterialTheme.typography.titleLarge
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Destino: ${viagem.destino}")

                                Text(
                                    "Data início: ${
                                        formatarData(viagem.dataInicio)
                                    }"
                                )

                                Text(
                                    "Data fim: ${
                                        formatarData(viagem.dataFim)
                                    }"
                                )

                                Text("Tipo: ${viagem.tipo}")

                                Text(
                                    "Orçamento: R$ ${
                                        viagem.orcamento
                                    }"
                                )

                                Text(
                                    "Total gastos: R$ ${
                                        viagem.totalGastos
                                    }"
                                )
                            }
                        }

                    } ?: Text(
                        "Nenhuma viagem encontrada para sua localização."
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Menu",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp
        )

        Divider()

        DrawerItem("Nova Viagem", Icons.Default.Add) {
            navController.navigate(Routes.NovaViagem.route)
        }

        DrawerItem("Minhas Viagens", Icons.Default.List) {
            navController.navigate("minhas_viagens")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        DrawerItem("Sobre", Icons.Default.Info) {
            navController.navigate(Routes.Sobre.route)
        }
    }
}

@Composable
fun DrawerItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)
    }
}


