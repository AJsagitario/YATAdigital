package com.example.billetera_digital.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.billetera_digital.model.UsuarioDto

class UserViewModel : ViewModel() {

    // -------- datos de perfil que muestras en pantallas --------
    var dni by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var alias by mutableStateOf("")
        private set

    var phone by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    // -------- datos que vienen del backend --------
    var saldo by mutableStateOf(0.0)
        private set

    var numeroTarjeta by mutableStateOf("")
        private set

    var ccv by mutableStateOf("")
        private set

    var fechaVencimiento by mutableStateOf("")
        private set

    // -------- switches --------
    var biometricsEnabled by mutableStateOf(true)
        private set

    var onlinePurchasesEnabled by mutableStateOf(true)
        private set

    var highAmountConfirmEnabled by mutableStateOf(true)
        private set

    // -------- llenar el VM con lo que llega del backend --------
    fun setFromDto(dto: UsuarioDto) {
        dni = dto.dni
        name = dto.nombre
        phone = dto.contacto ?: ""

        saldo = dto.saldo ?: 0.0
        numeroTarjeta = dto.numeroTarjeta ?: ""
        ccv = dto.ccv ?: ""
        fechaVencimiento = dto.fechaVencimiento ?: ""
        // alias y email no vienen, se quedan como estaban
    }

    // --- funciones públicas para editar desde la UI ---
    fun updateName(value: String) {
        name = value
    }

    fun updateAlias(value: String) {
        alias = value
    }

    fun updatePhone(value: String) {
        phone = value
    }

    fun updateEmail(value: String) {
        email = value
    }
    // --- switches ---
    fun toggleBiometrics(v: Boolean) { biometricsEnabled = v }
    fun toggleOnlinePurchases(v: Boolean) { onlinePurchasesEnabled = v }
    fun toggleHighAmountConfirm(v: Boolean) { highAmountConfirmEnabled = v }

    fun discountSaldo(monto: Double) {
        saldo -= monto
        if (saldo < 0)
            saldo = 0.0
    }
}
