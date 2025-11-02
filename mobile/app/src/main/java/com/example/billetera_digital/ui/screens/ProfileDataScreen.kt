package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.components.AppTopBar
import com.example.billetera_digital.ui.state.UserViewModel

@Composable
fun ProfileDataScreen(onBack: () -> Unit) {
    val vm: UserViewModel = viewModel()

    Scaffold(topBar = { AppTopBar(title = "Mi perfil", onBack = onBack) }) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = vm.name, onValueChange = {}, readOnly = true,
                label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = vm.alias, onValueChange = {}, readOnly = true,
                label = { Text("Alias") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = vm.phone, onValueChange = {}, readOnly = true,
                label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = vm.email, onValueChange = {}, readOnly = true,
                label = { Text("Correo") }, modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}
