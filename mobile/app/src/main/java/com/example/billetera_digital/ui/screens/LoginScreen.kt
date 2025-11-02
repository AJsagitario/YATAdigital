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

@Composable
fun LoginScreen(
    onOk: () -> Unit,
    onForgot: () -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val emailOk = email.contains("@") && email.contains(".")
    val passOk = pass.length >= 6
    val enabled = emailOk && passOk

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar sesión") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = email.isNotBlank() && !emailOk,
                supportingText = { if (email.isNotBlank() && !emailOk) Text("Correo no válido") }
            )

            OutlinedTextField(
                value = pass,
                onValueChange = { if (it.length <= 32) pass = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("PIN/Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = pass.isNotEmpty() && !passOk,
                supportingText = { if (pass.isNotEmpty() && !passOk) Text("Mínimo 6 caracteres") }
            )

            Button(
                onClick = onOk,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Ingresar") }

            TextButton(onClick = onForgot) { Text("Olvidé mi PIN/contraseña") }
            TextButton(onClick = onRegister) { Text("Crear cuenta") }
        }
    }
}
