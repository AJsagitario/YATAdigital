package com.example.billetera_digital.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Contact(val alias: String, val wallet: String)

class SendViewModel : ViewModel() {

    var contact: Contact? by mutableStateOf(null)
        private set

    var amount: String by mutableStateOf("0")
        private set

    var note: String by mutableStateOf("")
        private set

    // Evita colisión con el setter JVM generado por 'var amount'
    fun updateAmount(value: String) {
        amount = value
    }

    // Evita colisión con el setter JVM generado por 'var note'
    fun updateNote(value: String) {
        note = value
    }

    fun pick(c: Contact) {
        contact = c
    }

    fun clear() {
        contact = null
        amount = "0"
        note = ""
    }

    /**
     * Teclado numérico:
     * - solo dígitos y una coma
     * - máximo 2 decimales
     * - no permite cadenas vacías: mínimo "0"
     */
    fun press(ch: Char) {
        if (ch == ',') {
            if (!amount.contains(',')) amount += ','
            return
        }
        if (ch in '0'..'9') {
            amount = if (amount == "0") ch.toString() else amount + ch
            if (amount.count { it == ',' } == 1) {
                val parts = amount.split(',')
                if (parts.size == 2 && parts[1].length > 2) {
                    amount = parts[0] + "," + parts[1].take(2)
                }
            }
        }
    }

    fun backspace() {
        amount = if (amount.length <= 1) "0" else amount.dropLast(1)
    }

    fun amountDouble(): Double =
        amount.replace(',', '.').toDoubleOrNull() ?: 0.0
}
