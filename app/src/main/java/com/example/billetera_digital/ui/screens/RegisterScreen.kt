@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.SixDigitPinField

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar



@Composable
fun RegisterScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit = {}
) {
    var name   by rememberSaveable { mutableStateOf("") }
    var alias  by rememberSaveable { mutableStateOf("") }
    var phone  by rememberSaveable { mutableStateOf("") }
    var email  by rememberSaveable { mutableStateOf("") }
    var pin    by rememberSaveable { mutableStateOf("") }
    var pin2   by rememberSaveable { mutableStateOf("") }

    val emailOk = email.contains("@") && email.contains(".")
    val phoneOk = phone.filter(Char::isDigit).length in 6..12
    val pinOk   = pin.length == 6
    val pin2Ok  = pin2.length == 6
    val pinsEq  = pinOk && pin2Ok && pin == pin2

    val enabled = name.isNotBlank() &&
            alias.isNotBlank() &&
            phoneOk && emailOk &&
            pinsEq

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") }, singleLine = true
            )
            OutlinedTextField(
                value = alias, onValueChange = { alias = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("(@usuario)") }, singleLine = true
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it.filter(Char::isDigit) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Celular") }, singleLine = true,
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo") }, singleLine = true,
                isError = email.isNotBlank() && !emailOk,
                supportingText = {
                    if (email.isNotBlank() && !emailOk) Text("Correo no válido")
                }
            )

            Divider(Modifier.padding(top = 4.dp, bottom = 4.dp))

            SixDigitPinField(
                pin = pin,
                onPinChange = { pin = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Crea tu PIN (6 dígitos)"
            )
            SixDigitPinField(
                pin = pin2,
                onPinChange = { pin2 = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Confirma tu PIN"
            )
            if (pin2.isNotEmpty() && !pinsEq) {
                Text(
                    "Los PIN no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onContinue,   // TODO: guardar/usar el PIN tras OTP
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Continuar") }
        }
    }
}
