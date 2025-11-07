@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.state.Contact
import com.example.billetera_digital.ui.state.SendViewModel

@Composable
fun SearchContactScreen(
    vm: SendViewModel = viewModel(),
    onPicked: () -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    // mock de contactos
    val contacts = remember {
        listOf(
            Contact("@erika","9011223344"),
            Contact("@laura","9011223345"),
            Contact("@carlos","9011223346"),
            Contact("@sofia","9011223347"),
        )
    }
    val filtered = remember(query) {
        val q = query.trim().lowercase()
        when {
            q.isBlank() -> emptyList()
            else -> contacts.filter {
                it.alias.lowercase().contains(q) || it.wallet.contains(q)
            }
        }
    }
    val showEmptyState = query.isNotBlank() && filtered.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar contacto") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                placeholder = { Text("Alias o wallet") },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
                supportingText = {
                    if (query.isBlank()) Text("Escribe para buscar")
                }
            )

            if (showEmptyState) {
                Text("No se encontraron contactos", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered) { c ->
                        Surface(
                            tonalElevation = 1.dp,
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier.fillMaxWidth().clickable {
                                vm.pick(c)
                                onPicked()
                            }
                        ) {
                            ListItem(
                                headlineContent = { Text(c.alias) },
                                supportingContent = { Text(c.wallet) }
                            )
                        }
                    }
                }
            }
        }
    }
}
