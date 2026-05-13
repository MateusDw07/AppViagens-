package com.example.logincompose.telas

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.logincompose.AppDatabase
import com.example.logincompose.usuario.Viagem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NovaViagemScreen(userId: Int) {

    val context = LocalContext.current

    var destino by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }

    var dataInicio by remember { mutableStateOf<Long?>(null) }
    var dataFim by remember { mutableStateOf<Long?>(null) }

    var orcamento by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    fun abrirDatePicker(onDateSelected: (Long) -> Unit) {

        DatePickerDialog(
            context,
            { _, year, month, day ->

                calendar.set(year, month, day)

                onDateSelected(calendar.timeInMillis)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun formatarData(data: Long?): String {

        if (data == null) return ""

        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return formato.format(Date(data))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Nova Viagem",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = destino,
            onValueChange = { destino = it },
            label = { Text("Destino") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tipo,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(onClick = {

                abrirDatePicker {
                    dataInicio = it
                }

            }) {

                Text(
                    if (dataInicio == null)
                        "Data Início"
                    else
                        formatarData(dataInicio)
                )
            }

            Button(onClick = {

                abrirDatePicker {
                    dataFim = it
                }

            }) {

                Text(
                    if (dataFim == null)
                        "Data Fim"
                    else
                        formatarData(dataFim)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(onClick = { tipo = "Lazer" }) {
                Text("Lazer")
            }

            Button(onClick = { tipo = "Negócios" }) {
                Text("Negócios")
            }
        }

        OutlinedTextField(
            value = orcamento,
            onValueChange = { orcamento = it },
            label = { Text("Orçamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                if (
                    destino.isBlank() ||
                    tipo.isBlank() ||
                    dataInicio == null ||
                    dataFim == null ||
                    orcamento.isBlank()
                ) {

                    Toast.makeText(
                        context,
                        "Preencha todos os campos",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                val viagem = Viagem(
                    destino = destino,
                    tipo = tipo,
                    dataInicio = dataInicio!!,
                    dataFim = dataFim!!,
                    orcamento = orcamento.toDouble(),
                    userId = userId
                )

                CoroutineScope(Dispatchers.IO).launch {

                    val db = AppDatabase.getDatabase(context)

                    db.viagemDao().insert(viagem)
                }

                Toast.makeText(
                    context,
                    "Viagem cadastrada!",
                    Toast.LENGTH_SHORT
                ).show()

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Salvar Viagem")
        }
    }
}