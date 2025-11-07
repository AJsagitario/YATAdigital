package com.example.billetera_digital.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ui.theme.GradEnd
import com.example.billetera_digital.ui.theme.GradStart

@Composable
fun WalletCard(balance: Double, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Column(
            Modifier
                .background(Brush.linearGradient(listOf(GradStart, GradEnd)))
                .padding(20.dp)
        ) {
            Text("Saldo disponible", color = Color.White.copy(alpha = 0.9f))
            Text(
                text = "S/ " + "%,.2f".format(balance),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
