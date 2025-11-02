@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.FormTextField
import com.example.billetera_digital.ui.theme.FormBackground


@Composable
fun RegisterScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val aliasOk = alias.startsWith("@") && alias.length >= 3
    val phoneOk = phone.length in 9..12 && phone.all(Char::isDigit)
    val emailOk = email.contains("@") && email.contains(".")
    val enabled = name.isNotBlank() && aliasOk && phoneOk && emailOk

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear cuenta") }, navigationIcon = { /* ... */ }) },
        containerColor = FormBackground
    ) { p ->
        Column(Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(name,  { name = it },  "Nombre")
            FormTextField(alias, { alias = it }, "(@usuarioejemplo)",
                isError = alias.isNotBlank() && !aliasOk,
                supportingText = { if (alias.isNotBlank() && !aliasOk) Text("Debe iniciar con @ y tener + de 3 caracteres") }
            )
            FormTextField(phone, {
                if (it.length <= 12 && it.all(Char::isDigit)) phone = it
            }, "Celular",
                isError = phone.isNotEmpty() && !phoneOk,
                supportingText = { if (phone.isNotEmpty() && !phoneOk) Text("Entre 9 y 12 dígitos") }
            )
            FormTextField(email, { email = it }, "Correo",
                isError = email.isNotBlank() && !emailOk,
                supportingText = { if (email.isNotBlank() && !emailOk) Text("Correo no válido") }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onContinue,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Continuar") }
        }
    }
}
