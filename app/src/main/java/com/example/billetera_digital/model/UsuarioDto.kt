package com.example.billetera_digital.model

data class UsuarioDto(
    val dni: String,
    val nombre: String,
    val saldo: Double,
    val numeroTarjeta: String,
    val ccv: String,
    val fechaVencimiento: String,
    val contacto: String? = null
)