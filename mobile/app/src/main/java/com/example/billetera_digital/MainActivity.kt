package com.example.billetera_digital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.billetera_digital.ui.theme.BilleteraTheme
import com.example.billetera_digital.ui.screens.Routes   // <- importa el objeto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BilleteraTheme {
                Routes.AppNav()
            }
        }
    }
}
