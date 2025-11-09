package com.example.billetera_digital.model


data class UsuarioCreateRequest(
    val dni: String,
    val nombre: String,
    val contacto: String
)