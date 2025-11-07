@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SuccessScreen(
    alias: String,
    wallet: String,
    amount: String,
    onHome: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Listo") }) }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Transferencia realizada", style = MaterialTheme.typography.titleLarge)

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Monto", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("S/ $amount", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    HorizontalDivider()
                    Text("Destinatario", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(alias, style = MaterialTheme.typography.titleMedium)
                    Text("Wallet $wallet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Volver al inicio") }
        }
    }
}

/* Wrapper para no romper Previews existentes */
@Composable
fun SuccessScreen(onHome: () -> Unit) {
    SuccessScreen(alias = "-", wallet = "-", amount = "0.00", onHome = onHome)
}
