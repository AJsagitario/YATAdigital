package com.example.billetera_digital.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PinDots(count: Int, length: Int = 6) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(length) { i ->
            Surface(
                shape = RoundedCornerShape(50),
                color = if (i < count)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(8.dp)
            ) {}
        }
    }
}

/** Tarjeta + título + grid como el mock. */
@Composable
fun PinKeypad(
    onDigit: (Int) -> Unit,
    onDelete: () -> Unit,
    onScan: (() -> Unit)? = null,         // opcional
    title: String = "Ingresa tu clave"
) {
    //  null = celda vacía, -1 = borrar, -2 = scan
    val layout: List<List<Int?>> = listOf(
        listOf(2, 7, 0),
        listOf(6, 5, 8),
        listOf(3, 4, 9),
        listOf(-2, 1, -1),
    )

    Surface(
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp, bottomStart = 20.dp, bottomEnd = 20.dp),
        tonalElevation = 2.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            layout.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { cell ->
                        when (cell) {
                            null -> Spacer(Modifier.weight(1f).aspectRatio(1f))
                            -1 -> KeyDelete(Modifier.weight(1f), onDelete)
                            -2 -> KeyScan(Modifier.weight(1f)) { onScan?.invoke() }
                            else -> KeyDigit(cell, Modifier.weight(1f)) { onDigit(cell) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyDigit(digit: Int, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f).clickable(onClick = onClick).padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                digit.toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun KeyDelete(modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f).clickable(onClick = onClick).padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Backspace, contentDescription = "Borrar")
        }
    }
}

@Composable
private fun KeyScan(modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f).clickable(onClick = onClick).padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.QrCodeScanner, contentDescription = "Escanear")
        }
    }
}
