@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.state.SendViewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun AmountScreen(
    vm: SendViewModel = viewModel(),
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val contact = vm.contact
    val amountOk = vm.amountDouble() > 0.0
    val canContinue = amountOk && contact != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transferir") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (contact != null) {
                Card {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Para")
                        Text(contact.alias, style = MaterialTheme.typography.titleMedium)
                        Text("Wallet ${contact.wallet}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                Text("Selecciona un contacto antes de ingresar el monto",
                    color = MaterialTheme.colorScheme.error)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Monto", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = "S/ ${vm.amount}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if (!amountOk) {
                    Text("Ingresa un monto mayor a 0", color = MaterialTheme.colorScheme.error)
                }
            }

            Keypad(
                onDigit = { d -> vm.press(d) },
                onBackspace = { vm.backspace() }
            )

            OutlinedTextField(
                value = vm.note,
                onValueChange = { vm.updateNote(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Nota (opcional)") }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onNext,
                enabled = canContinue,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Continuar") }
        }
    }
}

/* ---------- keypad local ---------- */
@Composable
private fun Keypad(
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    showComma: Boolean = true
) {
    @Composable
    fun RowScope.key(label: String, ch: Char) {
        OutlinedButton(
            onClick = { onDigit(ch) },
            modifier = Modifier.weight(1f).height(56.dp)
        ) { Text(label) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            key("1", '1'); key("2", '2'); key("3", '3')
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            key("4", '4'); key("5", '5'); key("6", '6')
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            key("7", '7'); key("8", '8'); key("9", '9')
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (showComma) key(",", ',') else Spacer(Modifier.weight(1f).height(56.dp))
            key("0", '0')
            OutlinedButton(onClick = onBackspace, modifier = Modifier.weight(1f).height(56.dp)) {
                Text("←")
            }
        }
    }
}
