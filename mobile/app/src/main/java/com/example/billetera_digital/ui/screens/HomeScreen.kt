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
            BalanceCard(balance = 2458.50)

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
private fun BalanceCard(balance: Double) {
    val grad = Brush.linearGradient(
        listOf(Color(0xFF5B60F1), Color(0xFF8B92FF))
    )
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .background(grad)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text("Saldo disponible", color = Color.White.copy(alpha = 0.9f))
            Text(
                text = "S/ %,.2f".format(balance),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
