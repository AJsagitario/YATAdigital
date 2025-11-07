
package com.example.billetera_digital.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs

data class Movement(
    val alias: String,
    val amount: Double,
    val incoming: Boolean,   // true = ingreso, false = egreso
    val state: String,       // p.ej. "Confirmado"
    val date: String         // usa string para evitar APIs 26+
)

@Composable
fun MovementRow(m: Movement, modifier: Modifier = Modifier) {
    val amountColor = if (m.incoming) Color(0xFF1B8C4B) else Color(0xFFDB3F41)
    val chipBg = if (m.incoming) Color(0xFFDFF5E8) else Color(0xFFFCE4E4)
    val chipFg = if (m.incoming) Color(0xFF1B8C4B) else Color(0xFFDB3F41)

    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(chipBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (m.incoming) Icons.Outlined.ArrowDownward else Icons.Outlined.ArrowUpward,
                    contentDescription = null,
                    tint = chipFg
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(text = "@${m.alias}", style = MaterialTheme.typography.titleMedium)
                Row {
                    Text(
                        text = m.state,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = " · ${m.date}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = (if (m.incoming) "+S/ " else "-S/ ") + String.format("%,.2f", abs(m.amount)),
                color = amountColor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
