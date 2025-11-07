@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.PinDots
import com.example.billetera_digital.ui.components.PinKeypad

@Composable
fun PinCurrentScreen(
    onOk: () -> Unit,
    onBack: () -> Unit = {}
) {
    var pin by rememberSaveable { mutableStateOf("") }
    val enabled = pin.length == 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PIN actual") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Outlined.AccountBalanceWallet, null) } // icono visible a la izquierda del título
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier.padding(p).padding(horizontal = 16.dp, vertical = 8.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo centrado como en el mock
            Icon(
                imageVector = Icons.Outlined.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.height(8.dp))

            // Dots arriba del keypad
            PinDots(count = pin.length, length = 6)
            Spacer(Modifier.height(8.dp))

            // Tarjeta keypad con título
            PinKeypad(
                onDigit = { d -> if (pin.length < 6) pin += d.toString() },
                onDelete = { if (pin.isNotEmpty()) pin = pin.dropLast(1) },
                onScan = { /* TODO: acción de escaneo si aplica */ },
                title = "PIN Actual"
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onOk,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Continuar") }
        }
    }
}
