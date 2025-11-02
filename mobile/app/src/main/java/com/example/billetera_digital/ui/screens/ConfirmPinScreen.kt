@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.state.SendViewModel

@Composable
fun ConfirmPinScreen(
    vm: SendViewModel = viewModel(),
    onBack: () -> Unit,
    onOk: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val pinError = pin.isNotEmpty() && pin.length < 6
    val canConfirm = pin.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar operación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Vas a enviar")
                    Text("S/ ${vm.amount}", style = MaterialTheme.typography.headlineLarge)
                    HorizontalDivider()
                    Text("Para ${vm.contact?.alias ?: "-"}", style = MaterialTheme.typography.titleMedium)
                    Text("Wallet ${vm.contact?.wallet ?: "-"}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) pin = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("PIN de 6 dígitos") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                isError = pinError,
                supportingText = {
                    if (pinError) Text("El PIN debe tener 6 dígitos")
                }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onOk,
                enabled = canConfirm,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Confirmar") }
        }
    }
}
