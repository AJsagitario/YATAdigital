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
fun PinNewScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var pin by rememberSaveable { mutableStateOf("") }
    val enabled = pin.length == 6

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo PIN") }) }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.height(8.dp))
            PinDots(count = pin.length, length = 6)
            Spacer(Modifier.height(8.dp))

            PinKeypad(
                onDigit = { d -> if (pin.length < 6) pin += d.toString() },
                onDelete = { if (pin.isNotEmpty()) pin = pin.dropLast(1) },
                onScan = { /* opcional */ },
                title = "Nuevo PIN"
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { onNext(pin) },
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
