package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.state.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff

@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    onYapear: () -> Unit,
    onReceive: () -> Unit,
    onHistory: () -> Unit
) {
    val saldo = userViewModel.saldo   // 👈 ahora viene del backend

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Inicio", style = MaterialTheme.typography.headlineSmall)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Saldo disponible", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = "S/ ${"%,.2f".format(saldo)}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.VisibilityOff,
                    contentDescription = null
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onYapear,
                modifier = Modifier.weight(1f)
            ) {
                Text("Transferir")
            }
            Button(
                onClick = onReceive,
                modifier = Modifier.weight(1f)
            ) {
                Text("Recibir")
            }
            Button(
                onClick = onHistory,
                modifier = Modifier.weight(1f)
            ) {
                Text("Historial")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
