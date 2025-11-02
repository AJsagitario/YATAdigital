@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/* ---------- PERFIL: Datos ---------- */
@Composable
fun ProfileDataScreenStub(onBack: () -> Unit) {
    SimpleScaffold("Mis datos", onBack) {
        Text("Aquí van Nombre, Alias, Teléfono, Correo")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

/* ---------- PIN: Actual ---------- */
@Composable
fun PinCurrentScreen(onOk: () -> Unit, onBack: () -> Unit) {
    SimpleScaffold("PIN actual", onBack) {
        Text("Pantalla para validar el PIN actual")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onOk, modifier = Modifier.fillMaxWidth()) { Text("Continuar") }
    }
}

/* ---------- PIN: Nuevo ---------- */
@Composable
fun PinNewScreen(onOk: () -> Unit, onBack: () -> Unit) {
    SimpleScaffold("Nuevo PIN", onBack) {
        Text("Pantalla para ingresar el nuevo PIN")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onOk, modifier = Modifier.fillMaxWidth()) { Text("Continuar") }
    }
}

/* ---------- PIN: Confirmar ---------- */
@Composable
fun PinConfirmScreen(onOk: () -> Unit, onBack: () -> Unit) {
    SimpleScaffold("Confirmar PIN", onBack) {
        Text("Pantalla para confirmar el nuevo PIN")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onOk, modifier = Modifier.fillMaxWidth()) { Text("Confirmar") }
    }
}

/* ---------- PIN: Éxito ---------- */
@Composable
fun PinSuccessScreen(onHome: () -> Unit) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Listo") }) }) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            Text("PIN actualizado correctamente")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
        }
    }
}

/* ---------- Utilitario simple ---------- */
@Composable
private fun SimpleScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(title) }) }) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(16.dp)
                .fillMaxWidth(),
            content = content
        )
    }
}
