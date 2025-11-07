package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.components.AppTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import java.util.Locale
@Composable
fun HomeScreen(
    onYapear: () -> Unit,
    onReceive: () -> Unit,
    onHistory: () -> Unit
) {
    Scaffold(topBar = { AppTopBar(title = "Inicio") }) { p ->
        Column(
            Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // saldo mock
            BalanceCard(balance = 2458.50, modifier = Modifier.padding(horizontal = 16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onYapear,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) { Text("Transferir") }

                OutlinedButton(
                    onClick = onReceive,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) { Text("Recibir") }

                OutlinedButton(
                    onClick = onHistory,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) { Text("Historial") }
            }

            // aquí podrías listar movimientos mock
            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
private fun BalanceCard(
    balance: Double,
    modifier: Modifier = Modifier
) {
    var visible by rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Saldo disponible", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = if (visible) formatPen(balance) else "••••••",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            IconButton(onClick = { visible = !visible }) {
                val icon = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                val cd = if (visible) "Ocultar saldo" else "Mostrar saldo"
                Icon(imageVector = icon, contentDescription = cd)
            }
        }
    }
}

private fun formatPen(amount: Double): String =
    "S/ " + String.format(Locale("es", "PE"), "%,.2f", amount)