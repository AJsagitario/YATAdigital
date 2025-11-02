// ui/components/Keypad.kt
package com.example.billetera_digital.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
private fun RowScope.Key(label: String, ch: Char, onDigit: (Char) -> Unit) {
    OutlinedButton(
        onClick = { onDigit(ch) },
        modifier = Modifier.weight(1f).height(56.dp)
    ) { Text(label) }
}

@Composable
fun Keypad(
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    showComma: Boolean = true,
) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key("1", '1', onDigit); Key("2", '2', onDigit); Key("3", '3', onDigit)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key("4", '4', onDigit); Key("5", '5', onDigit); Key("6", '6', onDigit)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key("7", '7', onDigit); Key("8", '8', onDigit); Key("9", '9', onDigit)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (showComma) Key(",", ',', onDigit) else Spacer(Modifier.weight(1f).height(56.dp))
            Key("0", '0', onDigit)
            OutlinedButton(
                onClick = onBackspace,
                modifier = Modifier.weight(1f).height(56.dp)
            ) { Text("←") }
        }
    }
}
