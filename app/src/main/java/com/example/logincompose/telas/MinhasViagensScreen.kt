package com.example.logincompose.telas
import kotlinx.coroutines.withContext
import android.widget.Toast
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
import com.example.logincompose.AppDatabase
import com.example.logincompose.usuario.Viagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinhasViagensScreen(userId: Int) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var viagens by remember {
        mutableStateOf<List<Viagem>>(emptyList())
    }

    fun carregarViagens() {

        scope.launch {

            val db = AppDatabase.getDatabase(context)

            val lista = withContext(Dispatchers.IO) {

                db.viagemDao().listarPorUsuario(userId)
            }

            viagens = lista
        }
    }

    LaunchedEffect(Unit) {
        carregarViagens()
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

                    val db = AppDatabase.getDatabase(context)

                    withContext(Dispatchers.IO) {

                        db.viagemDao().delete(viagem)
                    }

                    carregarViagens()
                }


            }

            var mostrarDialog by remember {
                mutableStateOf(false)
            }

            var destinoEdit by remember {
                mutableStateOf(viagem.destino)
            }

            var tipoEdit by remember {
                mutableStateOf(viagem.tipo)
            }

            var orcamentoEdit by remember {
                mutableStateOf(viagem.orcamento.toString())
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

                            Text(
                                text = viagem.destino,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Data início: ${
                                formatarData(viagem.dataInicio)
                            }"
                        )

                        Text(
                            text = "Data fim: ${
                                formatarData(viagem.dataFim)
                            }"
                        )

                        Text(
                            text = "Orçamento: R$ ${viagem.orcamento}"
                        )
                    }
                }
            }
            if (mostrarDialog) {

                AlertDialog(

                    onDismissRequest = {
                        mostrarDialog = false
                    },

                    title = {
                        Text("Editar Viagem")
                    },

                    text = {

                        Column {

                            OutlinedTextField(
                                value = destinoEdit,
                                onValueChange = {
                                    destinoEdit = it
                                },
                                label = {
                                    Text("Destino")
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = tipoEdit,
                                onValueChange = {
                                    tipoEdit = it
                                },
                                label = {
                                    Text("Tipo")
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = orcamentoEdit,
                                onValueChange = {
                                    orcamentoEdit = it
                                },
                                label = {
                                    Text("Orçamento")
                                }
                            )
                        }
                    },

                    confirmButton = {

                        Button(
                            onClick = {

                                scope.launch {

                                    val db = AppDatabase.getDatabase(context)

                                    withContext(Dispatchers.IO) {

                                        db.viagemDao().update(

                                            viagem.copy(
                                                destino = destinoEdit,
                                                tipo = tipoEdit,
                                                orcamento = orcamentoEdit.toDouble()
                                            )
                                        )
                                    }

                                    carregarViagens()

                                    mostrarDialog = false
                                }
                            }
                        ) {

                            Text("Salvar")
                        }
                    },

                    dismissButton = {

                        Button(
                            onClick = {
                                mostrarDialog = false
                            }
                        ) {

                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }


}


fun formatarData(data: Long): String {

    val formato = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    return formato.format(Date(data))
}