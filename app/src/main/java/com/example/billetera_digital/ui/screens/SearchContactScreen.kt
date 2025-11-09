@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billetera_digital.ApiUtils
import com.example.billetera_digital.model.UsuarioDto
import com.example.billetera_digital.ui.state.SendViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SearchContactScreen(
    vm: SendViewModel,
    onPicked: () -> Unit,
    onBack: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var usuarios by remember { mutableStateOf<List<UsuarioDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    // cuando entra la pantalla, traemos la lista del backend
    LaunchedEffect(Unit) {
        loading = true
        ApiUtils.api.listarUsuarios()
            .enqueue(object : Callback<List<UsuarioDto>> {
                override fun onResponse(
                    call: Call<List<UsuarioDto>>,
                    response: Response<List<UsuarioDto>>
                ) {
                    loading = false
                    if (response.isSuccessful && response.body() != null) {
                        usuarios = response.body()!!
                    } else {
                        error = "No se pudo cargar usuarios"
                    }
                }

                override fun onFailure(call: Call<List<UsuarioDto>>, t: Throwable) {
                    loading = false
                    error = "Error de red: ${t.message}"
                }
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar contacto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Alias o wallet") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            when {
                loading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    // filtramos por nombre o por contacto (dni/cel)
                    val filtrados = usuarios.filter { u ->
                        val q = query.trim()
                        if (q.isBlank()) true
                        else {
                            u.nombre.contains(q, ignoreCase = true) ||
                                    (u.contacto?.contains(q, ignoreCase = true) ?: false) ||
                                    u.dni.contains(q, ignoreCase = true)
                        }
                    }

                    if (filtrados.isEmpty()) {
                        Text("No se encontraron usuarios")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filtrados) { user ->
                                UserRow(
                                    user = user,
                                    onClick = {
                                        // guardamos en el VM lo que eligió
                                        vm.pick(
                                            com.example.billetera_digital.ui.state.Contact(
                                                alias = user.nombre,              // o user.dni si prefieres
                                                wallet = user.dni               // tu backend usa dni/cel como id
                                            )
                                        )
                                        onPicked()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: UsuarioDto,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(user.nombre, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Wallet: ${user.dni}",
                style = MaterialTheme.typography.bodySmall
            )
            user.contacto?.let {
                Text(
                    text = "Cel: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
