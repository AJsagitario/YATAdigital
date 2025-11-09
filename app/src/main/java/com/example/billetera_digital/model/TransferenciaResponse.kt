package com.example.billetera_digital.model

import com.google.gson.annotations.SerializedName

data class TransferenciaResponse (
    @SerializedName("codigo_transferencia")
    val codigoTransferencia: String,
    val de: String,
    val a: String,
    val monto: Double,
    val mensaje: String?,
    val fecha: String
)
