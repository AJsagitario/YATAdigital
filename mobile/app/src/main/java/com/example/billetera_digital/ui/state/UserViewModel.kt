package com.example.billetera_digital.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var name   by mutableStateOf("Astrit Carolina")
        private set
    var alias  by mutableStateOf("@astrit")
        private set
    var phone  by mutableStateOf("999 888 777")
        private set
    var email  by mutableStateOf("astrit@example.com")
        private set

    var biometricsEnabled        by mutableStateOf(true)
        private set
    var onlinePurchasesEnabled   by mutableStateOf(false)
        private set
    var highAmountConfirmEnabled by mutableStateOf(true)
        private set

    fun toggleBiometrics(v: Boolean)        { biometricsEnabled = v }
    fun toggleOnlinePurchases(v: Boolean)   { onlinePurchasesEnabled = v }
    fun toggleHighAmountConfirm(v: Boolean) { highAmountConfirmEnabled = v }
}
