@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ApiUtils
import com.example.billetera_digital.model.TransferenciaRequest
import com.example.billetera_digital.model.TransferenciaResponse
import com.example.billetera_digital.ui.state.SendViewModel
import com.example.billetera_digital.ui.state.UserViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ConfirmPinScreen(
    vm: SendViewModel,
    userVm: UserViewModel,
    onBack: () -> Unit,
    onOk: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }

    var pin by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // datos que ya tenías en pantalla
    val amount = vm.amount
    val contact = vm.contact      // lo llenamos en SearchContactScreen
    val note = vm.note

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snack) },
        topBar = {
            TopAppBar(
                title = { Text("Confirmar con PIN") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // tarjeta con los datos
            androidx.compose.material3.Surface(
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Monto", style = MaterialTheme.typography.labelSmall)
                    Text("S/ $amount", style = MaterialTheme.typography.headlineSmall)

                    Spacer(Modifier.height(8.dp))
                    Text("Destinatario: ${contact?.alias ?: contact?.wallet ?: "-"}")
                    Text("DNI / wallet: ${contact?.wallet ?: "-"}")

                    if (note.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text("Nota: $note")
                    }
                }
            }

            Text("Ingresa tu PIN (6 dígitos)", style = MaterialTheme.typography.titleMedium)

            // ESTE es el campo que te faltaba realmente usar
            OutlinedTextField(
                value = pin,
                onValueChange = { new ->
                    // solo números y máximo 6
                    val filtered = new.filter { it.isDigit() }.take(6)
                    pin = filtered
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("PIN") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    // validaciones rápidas
                    if (contact == null) {
                        scope.launch {
                            snack.showSnackbar("Falta elegir destinatario")
                        }
                        return@Button
                    }
                    if (pin.length < 6) {
                        scope.launch {
                            snack.showSnackbar("PIN incompleto")
                        }
                        return@Button
                    }

                    val origen = userVm.dni
                    val destino = contact.wallet
                    val montoDouble = amount.toDoubleOrNull() ?: 0.0

                    if (origen.isBlank() || destino.isBlank()) {
                        scope.launch { snack.showSnackbar("Origen o destino vacío") }
                        return@Button
                    }
                    if (montoDouble <= 0.0) {
                        scope.launch { snack.showSnackbar("Monto inválido") }
                        return@Button
                    }

                    loading = true

                    val req = TransferenciaRequest(
                        origen = origen,
                        destino = destino,
                        monto = montoDouble,
                        mensaje = note.ifBlank { null }
                    )

                    ApiUtils.api.transferir(req)
                        .enqueue(object : Callback<TransferenciaResponse> {
                            override fun onResponse(
                                call: Call<TransferenciaResponse>,
                                response: Response<TransferenciaResponse>
                            ) {
                                loading = false
                                if (response.isSuccessful) {
                                    // bajamos saldo local
                                    userVm.discountSaldo(montoDouble)
                                    onOk()
                                } else {
                                    // leemos el error del backend si existe
                                    val msg = try {
                                        val err = response.errorBody()?.string()
                                        if (err != null) {
                                            val json = JSONObject(err)
                                            json.optString("error", "No se pudo transferir")
                                        } else {
                                            "No se pudo transferir"
                                        }
                                    } catch (e: Exception) {
                                        "No se pudo transferir"
                                    }
                                    scope.launch {
                                        snack.showSnackbar(msg)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<TransferenciaResponse>, t: Throwable) {
                                loading = false
                                scope.launch {
                                    snack.showSnackbar("Error de red: ${t.message}")
                                }
                            }
                        })
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Transferir")
                }
            }
        }
    }
}
