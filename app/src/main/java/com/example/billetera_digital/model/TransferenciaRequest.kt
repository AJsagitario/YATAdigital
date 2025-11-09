package com.example.billetera_digital.model

data class TransferenciaRequest (
    val origen: String,
    val destino: String,
    val monto: Double,
    val mensaje: String? = null
)