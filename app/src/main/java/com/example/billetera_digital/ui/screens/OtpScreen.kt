@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.SixDigitPinField
import com.example.billetera_digital.ui.screens.OtpScreen


@Composable
fun OtpScreen(
    onVerify: () -> Unit,
    onBack: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    val enabled = otp.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificación OTP") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ingresa el código de 6 dígitos enviado a tu correo")
            Spacer(Modifier.height(16.dp))
            SixDigitPinField(
                pin = otp,
                onPinChange = { otp = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Código OTP (6 dígitos)"
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onVerify,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Verificar") }
        }
    }
}
