@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ReceiveScreen(onBack: () -> Unit) {
    // mocks mínimos
    val alias = "@astrit"
    val wallet = "T02:BK1:SM409PZQSR"

    val clipboard: ClipboardManager = LocalClipboardManager.current
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recibir") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snack) }
    ) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Tu alias", style = MaterialTheme.typography.labelLarge)
                    Text(alias, style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider()
                    Text("Tu wallet", style = MaterialTheme.typography.labelLarge)
                    Text(wallet, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = {
                    clipboard.setText(AnnotatedString("$alias • $wallet"))
                    scope.launch { snack.showSnackbar("Datos copiados") }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Copiar alias y wallet") }

            // placeholder QR
            Card(Modifier.fillMaxWidth().height(220.dp)) {
                Box(Modifier.fillMaxSize()) { /* futuro: QR */ }
            }
        }
    }
}
