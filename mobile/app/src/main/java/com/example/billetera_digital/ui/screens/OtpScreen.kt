
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.text.KeyboardOptions




@Composable
fun OtpScreen(
    onVerify: () -> Unit,
    onBack: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    val ok = code.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificar código") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Ingresa el código de 6 dígitos enviado a tu correo o SMS")

            OutlinedTextField(
                value = code,
                onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) code = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Código") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = code.isNotEmpty() && !ok,
                supportingText = { if (code.isNotEmpty() && !ok) Text("Debe tener 6 dígitos") }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onVerify,
                enabled = ok,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Verificar") }
        }
    }
}
