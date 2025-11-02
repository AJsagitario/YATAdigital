package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.AppTopBar

@Composable
fun SendConfirmScreen(
    amount: String = "0",
    toAlias: String = "@usuario",
    wallet: String = "T02:XXXX",
    onOk: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(topBar = { AppTopBar(title = "Confirmar operación", onBack = onBack) }) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Vas a enviar", style = MaterialTheme.typography.labelLarge)
                    Text("S/ $amount", style = MaterialTheme.typography.headlineLarge)
                    Divider()
                    Text("Para", style = MaterialTheme.typography.labelLarge)
                    Text(toAlias, style = MaterialTheme.typography.titleMedium)
                    Text("Wallet  $wallet", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                "Ingresa tu PIN de seguridad",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = onOk, modifier = Modifier.fillMaxWidth()) {
                Text("Confirmar")
            }
        }
    }
}
