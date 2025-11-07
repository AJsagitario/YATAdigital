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

@Composable
fun NewPinScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var pin by rememberSaveable { mutableStateOf("") }
    val enabled = pin.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo PIN") },
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
            PinDots(count = pin.length, length = 6)
            Spacer(Modifier.height(8.dp))

            PinKeypad(
                onDigit = { d -> if (pin.length < 6) pin += d.toString() },
                onDelete = { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onNext(pin) },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Continuar") }
        }
    }
}
