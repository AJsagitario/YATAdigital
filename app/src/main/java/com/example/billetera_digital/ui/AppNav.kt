package com.example.billetera_digital.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.state.SendViewModel
import androidx.compose.runtime.remember

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.History

import com.example.billetera_digital.ui.screens.OtpScreen


object Routes {
    const val Splash = "splash"
    const val Home = "home"
    const val History = "history"
    const val Profile = "profile"
    const val ProfileData = "profile_data"
    const val PinCurrent = "pin_current"
    const val PinNew = "pin_new"
    const val PinConfirm = "pin_confirm"
    const val PinSuccess = "pin_success"

    // Enviar dinero
    const val Search = "search_contact"
    const val Amount = "amount"
    const val SendConfirm = "send_confirm"
    const val Success = "success"
    const val Receive = "receive"
    const val Login = "login"
    const val Register = "register"
    const val Otp = "otp"

    private data class Tab(val route: String, val label: String, val icon: ImageVector)

    @Composable

    fun AppNav() {
        val nav = rememberNavController()

        data class Tab(val route: String, val label: String, val icon: ImageVector)

        val tabs = listOf(
            Tab(Routes.Home, "Inicio", Icons.Outlined.Home),
            Tab(Routes.History, "Historial", Icons.Outlined.History),
            Tab(Routes.Profile, "Perfil", Icons.Outlined.Person)
        )

        Scaffold(
            bottomBar = {
                val dest = nav.currentBackStackEntryAsState().value?.destination
                val showBar = dest?.route in tabs.map { it.route }
                if (showBar) {
                    NavigationBar {
                        tabs.forEach { t ->
                            val selected = dest?.route == t.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (!selected) nav.navigate(t.route) {
                                        popUpTo(Routes.Home) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                icon = { Icon(t.icon, contentDescription = null) },
                                label = { Text(t.label) }
                            )
                        }
                    }
                }
            }
        ) { p ->
            NavHost(
                navController = nav,
                startDestination = Routes.Splash,
                modifier = Modifier.padding(p),
                route = "root" // scope para el VM compartido
            ) {
                composable(Routes.Splash) {
                    SplashScreen(
                        onCreate = {
                            nav.navigate(Routes.Register) {
                                popUpTo(Routes.Splash) {
                                    inclusive = false
                                }
                            }
                        },
                        onLogin = {
                            nav.navigate(Routes.Login) {
                                popUpTo(Routes.Splash) {
                                    inclusive = false
                                }
                            }
                        }
                    )
                }

                composable(Routes.Login) {
                    LoginScreen(
                        onOk = {
                            nav.navigate(Routes.Home) {
                                popUpTo(Routes.Home) {
                                    inclusive = true
                                }
                            }
                        },
                        onForgot = { nav.navigate(Routes.PinCurrent) },
                        onRegister = { nav.navigate(Routes.Register) },
                        onBack = { nav.popBackStack() }
                    )
                }

                composable(Routes.Register) {
                    RegisterScreen(
                        onContinue = { nav.navigate(Routes.Otp) },
                        onBack = { nav.popBackStack() })
                }

                composable(Routes.Otp) {
                    OtpScreen(
                        onVerify = {
                            nav.navigate(Routes.Home) {
                                popUpTo(Routes.Home) {
                                    inclusive = true
                                }
                            }
                        },
                        onBack = { nav.popBackStack() }
                    )
                }

                // Principal
                composable(Routes.Home) {
                    HomeScreen(
                        onYapear = { nav.navigate(Routes.Search) },
                        onReceive = { nav.navigate(Routes.Receive) },
                        onHistory = { nav.navigate(Routes.History) }
                    )
                }
                composable(Routes.History) { HistoryScreen() }

                // Perfil
                composable(Routes.Profile) {
                    ProfileScreen(
                        onOpenData = { nav.navigate(Routes.ProfileData) },
                        onChangePin = { nav.navigate(Routes.PinCurrent) },
                        onLogout = {
                            nav.navigate(Routes.Splash) {
                                popUpTo(Routes.Home) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(Routes.ProfileData) { ProfileDataScreen(onBack = { nav.popBackStack() }) }

                // 1) PIN actual -> Nuevo PIN
                composable(Routes.PinCurrent) {
                    PinCurrentScreen(
                        onOk   = { nav.navigate(Routes.PinNew) },
                        onBack = { nav.popBackStack() }
                    )
                }

                // 2) Nuevo PIN: guarda en SavedState y navega
                composable(Routes.PinNew) {
                    PinNewScreen(
                        onNext = { newPin ->
                            nav.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("newPin", newPin)
                            nav.navigate(Routes.PinConfirm)
                        },
                        onBack = { nav.popBackStack() }
                    )
                }

                // 3) Confirmar: lee el PIN guardado
                composable(Routes.PinConfirm) {
                    val firstPin = nav.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<String>("newPin") ?: ""

                    PinConfirmScreen(
                        firstPin = firstPin,
                        onOk     = { nav.navigate(Routes.PinSuccess) },
                        onBack   = { nav.popBackStack() }
                    )
                }

                composable(Routes.PinSuccess) {
                    PinSuccessScreen(onHome = {
                        nav.navigate(Routes.Profile) {
                            popUpTo(Routes.Profile) {
                                inclusive = true
                            }
                        }
                    })
                }

                // Envío: Buscar contacto
                composable(Routes.Search) { backStackEntry ->
                    val parent = remember(backStackEntry) { nav.getBackStackEntry("root") }
                    val vm: SendViewModel = viewModel(parent)
                    SearchContactScreen(
                        vm = vm,
                        onPicked = { nav.navigate(Routes.Amount) },
                        onBack = { nav.popBackStack() }
                    )
                }

                // Envío: Monto
                composable(Routes.Amount) { backStackEntry ->
                    val parent = remember(backStackEntry) { nav.getBackStackEntry("root") }
                    val vm: SendViewModel = viewModel(parent)
                    AmountScreen(
                        vm = vm,
                        onNext = { nav.navigate(Routes.SendConfirm) },
                        onBack = { nav.popBackStack() }
                    )
                }

                // Envío: Confirmar con PIN
                composable(Routes.SendConfirm) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) { nav.getBackStackEntry("root") }
                    val vm: SendViewModel = viewModel(parentEntry)
                    ConfirmPinScreen(
                        vm = vm,
                        onBack = { nav.popBackStack() },
                        onOk = {
                            nav.navigate(Routes.Success) {
                                popUpTo(Routes.Home) {
                                    inclusive = false
                                }
                            }
                        }
                    )
                }

                // Recibir
                composable(Routes.Receive) { ReceiveScreen(onBack = { nav.popBackStack() }) }

                // Éxito
                composable(Routes.Success) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) { nav.getBackStackEntry("root") }
                    val vm: SendViewModel = viewModel(parentEntry)

                    SuccessScreen(
                        alias  = vm.contact?.alias ?: "-",
                        wallet = vm.contact?.wallet ?: "-",
                        amount = vm.amount,
                        onHome = {
                            vm.clear()
                            nav.navigate(Routes.Home) {
                                popUpTo(Routes.Home) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}


