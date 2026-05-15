package com.example.logincompose.telas

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logincompose.AppDatabase
import com.example.logincompose.repository.ViagemRepository
import com.example.logincompose.usuario.Viagem
import com.example.logincompose.viewmodel.ViagemViewModel
import com.example.logincompose.viewmodel.ViagemViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinhasViagensScreen(userId: Int) {

    val context = LocalContext.current

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val repository = remember {
        ViagemRepository(db.viagemDao())
    }

    val factory = remember {
        ViagemViewModelFactory(repository)
    }

    val viewModel: ViagemViewModel =
        viewModel(factory = factory)

    val viagens by viewModel.viagens.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarViagens(userId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(viagens, key = { it.id }) { viagem ->

            val dismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(dismissState.currentValue) {

                if (
                    dismissState.currentValue ==
                    SwipeToDismissBoxValue.EndToStart ||

                    dismissState.currentValue ==
                    SwipeToDismissBoxValue.StartToEnd
                ) {

                    viewModel.deletarViagem(
                        viagem,
                        userId
                    )
                }
            }

            var mostrarDialog by remember {
                mutableStateOf(false)
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {}
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {},
                            onLongClick = {
                                mostrarDialog = true
                            }
                        )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            if (viagem.tipo == "Lazer") {

                                Icon(
                                    imageVector = Icons.Default.Flight,
                                    contentDescription = null
                                )

                            } else {

                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null
                                )
                            }

                            Text(viagem.destino)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Tipo: ${viagem.tipo}")
                        Text("Orçamento: R$ ${viagem.orcamento}")


                        Text(
                            "Início: ${formatarData(viagem.dataInicio)}"
                        )

                        Text(
                            "Fim: ${formatarData(viagem.dataFim)}"
                        )
                    }
                }
            }
        }
    }
}
