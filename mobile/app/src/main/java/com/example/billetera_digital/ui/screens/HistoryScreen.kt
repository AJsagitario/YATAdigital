@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


private enum class Status { DONE, PENDING, CANCELED }
private data class Movement(
    val id: String,
    val alias: String,
    val wallet: String,
    val amount: Double,
    val note: String,
    val daysAgo: Int,
    val sent: Boolean,
    val status: Status
)

@Composable
fun HistoryScreen() {
    val all = remember {
        listOf(
            Movement("1","@erika","9011223344",  25.0,"Café",   0,true,  Status.DONE),
            Movement("2","@laura","9011223345", 120.0,"Alquiler",2,true, Status.DONE),
            Movement("3","@carlos","9011223346", 60.0,"Devolución",1,false,Status.DONE),
            Movement("4","@sofia","9011223347", 18.5,"Taxi",   6,true,  Status.CANCELED),
            Movement("5","@ana","9011223348",   9.9,"Snacks", 10,true,  Status.DONE),
        )
    }

    var filter by remember { mutableStateOf("Hoy") }
    val filtered = remember(filter, all) {
        when (filter) {
            "Hoy" -> all.filter { it.daysAgo == 0 }
            "Semana" -> all.filter { it.daysAgo <= 7 }
            else -> all
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Historial") }) }) { p ->
        Column(
            Modifier.padding(p).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = filter == "Hoy",
                    onClick = { filter = "Hoy" },
                    label = { Text("Hoy") }
                )
                FilterChip(
                    selected = filter == "Semana",
                    onClick = { filter = "Semana" },
                    label = { Text("Semana") }
                )
                FilterChip(
                    selected = filter == "Todos",
                    onClick = { filter = "Todos" },
                    label = { Text("Todos") }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { m -> MovementRow(m) }
            }
        }
    }
}

@Composable
private fun MovementRow(m: Movement) {
    val sign = if (m.sent) "-" else "+"
    val amountText = "${if (m.sent) "-" else "+"} S/ %,.2f".format(m.amount)
    val color = if (m.sent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

    Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.large) {
        ListItem(
            headlineContent = { Text(m.alias) },
            supportingContent = {
                val st = when (m.status) {
                    Status.DONE -> "Completado"
                    Status.PENDING -> "Pendiente"
                    Status.CANCELED -> "Cancelado"
                }
                Text("$st • ${m.note}")
            },
            trailingContent = { Text(amountText, color = color) }
        )
    }
}
