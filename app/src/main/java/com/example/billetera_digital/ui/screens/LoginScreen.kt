@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ApiUtils
import com.example.billetera_digital.R
import com.example.billetera_digital.model.UsuarioDto
import com.example.billetera_digital.ui.state.UserViewModel

private val CardPurple = Color(0xFF6366F1)
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
                    .background(
                        Color.White.copy(alpha = if (index == 0) 1.0f else 0.5f),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,        // ⬅️ ahora sí lo recibimos
    onOk: () -> Unit,
    onForgot: () -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit
) {
    var ident by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var show by rememberSaveable { mutableStateOf(false) }

    // validación de usuario/teléfono
    val isAlias = ident.startsWith("@")
    val aliasOk = Regex("^@[A-Za-z0-9_]{3,15}$").matches(ident)
    val onlyDigits = ident.filter(Char::isDigit)
    val phonePeru = when {
        // 9 dígitos peruanos
        onlyDigits.length == 9 && onlyDigits.startsWith("9") -> onlyDigits
        // +51 + 9 dígitos
        onlyDigits.length == 11 && onlyDigits.startsWith("51") && onlyDigits.drop(2).startsWith("9") ->
            onlyDigits.takeLast(9)
        else -> ""
    }
    val phoneOk = phonePeru.isNotEmpty()
    val idOk = (isAlias && aliasOk) || (!isAlias && phoneOk)

    // tu backend usa PIN de 6
    val passOk = pass.length == 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_wallet),
            contentDescription = "Logo YATA",
            modifier = Modifier.size(72.dp)
        )

        Text("YATA", style = MaterialTheme.typography.headlineSmall, color = CardPurple)
        Text("Tu billetera digital", color = BrandSubtitle)

        Spacer(Modifier.height(30.dp))

        Surface(
            color = CardPurple,
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginDots()
                Spacer(Modifier.height(8.dp))

                // usuario / celular
                OutlinedTextField(
                    value = ident,
                    onValueChange = { ident = it.trim() },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ingresa tu usuario o número de celular") },
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
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    isError = ident.isNotBlank() && !idOk,
                    supportingText = {
                        if (ident.isNotBlank() && !idOk) {
                            Text(
                                if (isAlias) "Ejemplo: @usuario"
                                else "Acepta 9 dígitos o +51 9XXXXXXXX"
                            )
                        }
                    }
                )

                // PIN
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ingresa tu PIN") },
                    singleLine = true,
                    visualTransformation =
                        if (show) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
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
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    isError = pass.isNotEmpty() && !passOk,
                    supportingText = {
                        if (pass.isNotEmpty() && !passOk) Text("Debe tener 6 dígitos")
                    }
                )

                Button(
                    onClick = {
                        val numero = phonePeru
                        val pin = pass

                        if (numero.isNotEmpty() && pin.length == 6) {
                            ApiUtils.api.loginPorContactoYPin(numero, pin)
                                .enqueue(object : retrofit2.Callback<UsuarioDto> {
                                    override fun onResponse(
                                        call: retrofit2.Call<UsuarioDto>,
                                        response: retrofit2.Response<UsuarioDto>
                                    ) {
                                        val body = response.body()
                                        if (response.isSuccessful && body != null) {
                                            // ⬅️ ahora sí existe el vm
                                            userViewModel.setFromDto(body)
                                            onOk()
                                        } else {
                                            // número o pin incorrecto
                                        }
                                    }

                                    override fun onFailure(
                                        call: retrofit2.Call<UsuarioDto>,
                                        t: Throwable
                                    ) {
                                        // error de red
                                    }
                                })
                        }
                    },
                    enabled = idOk && passOk,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Ingresar")
                }

                Spacer(Modifier.weight(1f))

                TextButton(onClick = onRegister) {
                    Text(
                        "Crear cuenta",
                        color = CardPurple,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
