@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.PinDots
import com.example.billetera_digital.ui.components.PinKeypad
import com.example.billetera_digital.ui.state.SendViewModel

@Composable
fun ConfirmPinScreen(
    vm: SendViewModel,
    onBack: () -> Unit,
    onOk: () -> Unit
) {
    var pin by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val enabled = pin.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar con PIN") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Resumen del envío
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Monto")
                    Text("S/ ${vm.amount}", style = MaterialTheme.typography.headlineSmall)
                    Text("Destinatario: ${vm.contact?.alias ?: "-"}")
                    Text("Wallet: ${vm.contact?.wallet ?: "-"}")
                }
            }

            // Indicadores y teclado
            PinDots(count = pin.length, length = 6)
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            PinKeypad(
                onDigit = { d -> if (pin.length < 6) pin += d.toString() },
                onDelete = { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // TODO: validar contra backend si aplica
                    error = null
                    onOk()
                },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Confirmar") }
        }
    }
}
