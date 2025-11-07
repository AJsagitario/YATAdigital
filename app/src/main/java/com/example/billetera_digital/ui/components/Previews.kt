package com.example.billetera_digital.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.billetera_digital.ui.theme.BilleteraTheme
import com.example.billetera_digital.ui.screens.*

@Preview(showSystemUi = true, showBackground = true, name = "Splash")
@Composable fun Preview_Splash() {
    BilleteraTheme { SplashScreen(onCreate = {}, onLogin = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "Home")
@Composable fun Preview_Home() {
    BilleteraTheme { HomeScreen(onYapear = {}, onReceive = {}, onHistory = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "History")
@Composable fun Preview_History() {
    BilleteraTheme { HistoryScreen() }
}

@Preview(showSystemUi = true, showBackground = true, name = "Profile")
@Composable fun Preview_Profile() {
    BilleteraTheme { ProfileScreen(onOpenData = {}, onChangePin = {}, onLogout = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "ProfileData")
@Composable fun Preview_ProfileData() {
    BilleteraTheme { ProfileDataScreen(onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinCurrent")
@Composable fun Preview_PinCurrent() {
    BilleteraTheme { PinCurrentScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinNew")
@Composable fun Preview_PinNew() {
    BilleteraTheme { PinNewScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinConfirm")
@Composable fun Preview_PinConfirm() {
    BilleteraTheme { PinConfirmScreen(onOk = {}, onBack = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "PinSuccess")
@Composable fun Preview_PinSuccess() {
    BilleteraTheme { PinSuccessScreen(onHome = {}) }
}

@Preview(showSystemUi = true, showBackground = true, name = "Success")
@Composable fun Preview_Success() {
    BilleteraTheme {
        SuccessScreen(alias = "@erika", wallet = "9011223344", amount = "150.00", onHome = {})
    }
}
