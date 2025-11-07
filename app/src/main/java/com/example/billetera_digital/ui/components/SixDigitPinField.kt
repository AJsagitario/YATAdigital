package com.example.billetera_digital.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun SixDigitPinField(
    pin: String,
    onPinChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "PIN (6 dígitos)"
) {
    OutlinedTextField(
        value = pin.filter(Char::isDigit).take(6),
        onValueChange = { onPinChange(it.filter(Char::isDigit).take(6)) },
        modifier = modifier,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        supportingText = {
            if (pin.isNotEmpty() && pin.length != 6) Text("Debe tener 6 dígitos")
        }
    )
}
