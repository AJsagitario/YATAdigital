package com.example.billetera_digital.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.billetera_digital.model.UsuarioDto
import com.example.billetera_digital.ui.screens.*
import com.example.billetera_digital.ui.state.UserViewModel
import com.example.billetera_digital.ui.theme.BilleteraTheme

// pequeño helper solo para previews
private fun fakeUserVm(): UserViewModel {
    val vm = UserViewModel()
    // armamos un DTO de mentira con los mismos campos que usa tu app
    vm.setFromDto(
        UsuarioDto(
            dni = "12345678",
            nombre = "Usuario Demo",
            contacto = "999888777",
            saldo = 2458.50,
            numeroTarjeta = "4111111111111111",
            ccv = "123",
            fechaVencimiento = "12/29"
        )
    )
    return vm
}

@Preview(showSystemUi = true, showBackground = true, name = "Splash")
@Composable
fun Preview_Splash() {
    BilleteraTheme {
        SplashScreen(onCreate = {}, onLogin = {})
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Home")
@Composable
fun Preview_Home() {
    val vm = fakeUserVm()
    BilleteraTheme {
        HomeScreen(
            userViewModel = vm,
            onYapear = {},
            onReceive = {},
            onHistory = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "History")
@Composable
fun Preview_History() {
    BilleteraTheme { HistoryScreen() }
}

@Preview(showSystemUi = true, showBackground = true, name = "Profile")
@Composable
fun Preview_Profile() {
    BilleteraTheme {
        ProfileScreen(
            onOpenData = {},
            onChangePin = {},
            onLogout = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "ProfileData")
@Composable
fun Preview_ProfileData() {
    val vm = fakeUserVm()
    BilleteraTheme {
        ProfileDataScreen(
            userViewModel = vm,
            onBack = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinCurrent")
@Composable
fun Preview_PinCurrent() {
    BilleteraTheme { PinCurrentScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinNew")
@Composable
fun Preview_PinNew() {
    BilleteraTheme { PinNewScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinConfirm")
@Composable
fun Preview_PinConfirm() {
    BilleteraTheme { PinConfirmScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinSuccess")
@Composable
fun Preview_PinSuccess() {
    BilleteraTheme { PinSuccessScreen(onHome = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "Success")
@Composable
fun Preview_Success() {
    BilleteraTheme {
        SuccessScreen(
            alias = "@erika",
            wallet = "9011223344",
            amount = "150.00",
            onHome = {}
        )
    }
}

