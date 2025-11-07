@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onOpenData: () -> Unit,
    onChangePin: () -> Unit,
    onLogout: () -> Unit,
) {
    var biometrics by remember { mutableStateOf(false) }
    var ecommerce   by remember { mutableStateOf(false) }
    var highConfirm by remember { mutableStateOf(false) }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Mi perfil") }) }) { p ->
        LazyColumn(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            item { Text("Mi cuenta", style = MaterialTheme.typography.titleMedium) }

            item {
                ListItem(
                    modifier = Modifier.clickable { onOpenData() },
                    leadingContent = { Icon(imageVector = Icons.Outlined.ManageAccounts, contentDescription = null) },
                    headlineContent = { Text("Mis datos") },
                    supportingContent = { Text("Nombre, alias, teléfono, correo") },
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { onChangePin() },
                    leadingContent = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) },
                    headlineContent = { Text("Cambiar mi clave") },
                    supportingContent = { Text("PIN de 6 dígitos") },
                )
            }
            item {
                ListItem(
                    leadingContent = { Icon(imageVector = Icons.Outlined.QrCode, contentDescription = null) },
                    headlineContent = { Text("Mi QR") },
                    supportingContent = { Text("Próximamente") },
                )
            }
            item {
                ListItem(
                    leadingContent = { Icon(imageVector = Icons.Outlined.Place, contentDescription = null) },
                    headlineContent = { Text("Mis direcciones") },
                    supportingContent = { Text("Próximamente") },
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            item { Text("Ajustes", style = MaterialTheme.typography.titleMedium) }



            item {
                SettingSwitchRow(
                    title = "Confirmación de transferencia monto alto",
                    checked = highConfirm,
                    onCheckedChange = { highConfirm = it }
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                FilledTonalButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión")
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text(
                    "Versión 0.1 • Términos y Condiciones • Política de Privacidad",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
