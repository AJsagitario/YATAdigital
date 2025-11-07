@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.R


private val CardPurple = Color(0xFF6366F1)
private val CardPurpleAlt = Color(0xFF6F73F3)
private val BrandSubtitle = Color(0xFF6B7A99)

@Composable
private fun LoginDots(modifier: Modifier = Modifier, total: Int = 4) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.White.copy(alpha = if (index == 0) 1.0f else 0.5f), CircleShape)
            )
        }
    }
}

@Composable
fun LoginScreen(
    onOk: () -> Unit,
    onForgot: () -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit
) {
    // Estado y Validaciones (sin cambios)
    var ident by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var show by rememberSaveable { mutableStateOf(false) }

    val isAlias = ident.startsWith("@")
    val aliasOk = Regex("^@[A-Za-z0-9_]{3,15}$").matches(ident)
    val onlyDigits = ident.filter(Char::isDigit)
    val phonePeru = when {
        onlyDigits.length == 9 && onlyDigits.startsWith("9") -> onlyDigits
        onlyDigits.length == 11 && onlyDigits.startsWith("51") && onlyDigits.drop(2).startsWith("9") -> onlyDigits.takeLast(9)
        else -> ""
    }
    val phoneOk = phonePeru.isNotEmpty()
    val idOk = (isAlias && aliasOk) || (!isAlias && phoneOk)
    val passOk = pass.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_wallet),
            contentDescription = "Logo YATA",
            modifier = Modifier.size(72.dp)
        )

        Text("YATA", style = MaterialTheme.typography.headlineSmall, color = CardPurple)
        Text("Tu billetera digital", color = BrandSubtitle)

        Spacer(Modifier.height(30.dp))

        // Tarjeta morada
        Surface(
            color = CardPurple,
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 16.dp,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginDots()
                Spacer(Modifier.height(8.dp))

                // --- Usuario o celular (Colores ajustados) ---
                OutlinedTextField(
                    value = ident,
                    onValueChange = { ident = it.trim() },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ingresa tu usuario o numero de celular") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        errorContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                        // --- AJUSTES DE COLOR ---
                        focusedLabelColor = Color.Gray, // Label enfocado en gris
                        unfocusedLabelColor = Color.Gray, // Label desenfocado en gris
                        errorSupportingTextColor = Color.White.copy(alpha = 0.9f) // Texto de error en blanco
                        // --- FIN DE AJUSTES ---
                    ),
                    shape = RoundedCornerShape(12.dp),
                    isError = ident.isNotBlank() && !idOk,
                    supportingText = {
                        if (ident.isNotBlank() && !idOk) {
                            Text(if (isAlias) "Ejemplo: @usuario" else "Acepta 9 dígitos o +51 9XXXXXXXX")
                        }
                    }
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ingresa tu contraseña") },
                    singleLine = true,
                    visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { show = !show }) {
                            Icon(
                                if (show) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        errorContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                        // --- AJUSTES DE COLOR ---
                        focusedLabelColor = Color.Gray, // Label enfocado en gris
                        unfocusedLabelColor = Color.Gray, // Label desenfocado en gris
                        errorSupportingTextColor = Color.White.copy(alpha = 0.9f) // Texto de error en blanco
                        // --- FIN DE AJUSTES ---
                    ),
                    shape = RoundedCornerShape(12.dp),
                    isError = pass.isNotEmpty() && !passOk,
                    supportingText = { if (pass.isNotEmpty() && !passOk) Text("Mínimo 6 caracteres") }
                )

                // Botón morado
                Button(
                    onClick = {
                        val normalizedId = if (isAlias) ident.lowercase() else phonePeru
                        // TODO: autenticar(normalizedId, pass)
                        onOk()
                    },
                    enabled = idOk && passOk,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardPurpleAlt,
                        contentColor = Color.White,
                        disabledContainerColor = CardPurpleAlt.copy(alpha = 0.35f),
                        disabledContentColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) { Text("Ingresar") }

                TextButton(onClick = onForgot) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        // Botón "Crear cuenta"
        TextButton(onClick = onRegister) {
            Text(
                "Crear cuenta",
                color = CardPurple,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}