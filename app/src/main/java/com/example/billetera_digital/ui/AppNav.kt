package com.example.billetera_digital.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billetera_digital.ui.state.SendViewModel
import com.example.billetera_digital.ui.state.UserViewModel

// ================== RUTAS ==================
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

    // auth
    const val Login = "login"
    const val Register = "register"
    const val Otp = "otp"
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    // compartido entre login/register/perfil
    val userViewModel: UserViewModel = viewModel()

    data class Tab(val route: String, val label: String, val icon: ImageVector)
    val tabs = listOf(
        Tab(Routes.Home, "Inicio", Icons.Outlined.Home),
        Tab(Routes.History, "Historial", Icons.Outlined.History),
        Tab(Routes.Profile, "Perfil", Icons.Outlined.Person)
    )

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            val showBar = currentRoute in tabs.map { it.route }

            if (showBar) {
                NavigationBar {
                    tabs.forEach { t ->
                        val selected = currentRoute == t.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(t.route) {
                                        popUpTo(Routes.Home) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = { Icon(t.icon, contentDescription = null) },
                            label = { Text(t.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Splash,
            modifier = Modifier.padding(innerPadding),
            route = "root"        // scope para compartir SendViewModel
        ) {
            // ========== SPLASH ==========
            composable(Routes.Splash) {
                SplashScreen(
                    onCreate = {
                        navController.navigate(Routes.Register) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    },
                    onLogin = {
                        navController.navigate(Routes.Login) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    }
                )
            }

            // ========== LOGIN ==========
            composable(Routes.Login) {
                LoginScreen(
                    userViewModel = userViewModel,
                    onOk = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    },
                    onForgot = { navController.navigate(Routes.PinCurrent) },
                    onRegister = { navController.navigate(Routes.Register) },
                    onBack = { navController.popBackStack() }
                )
            }

            // ========== REGISTER ==========
            composable(Routes.Register) {
                RegisterScreen(
                    userViewModel = userViewModel,
                    onContinue = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ========== OTP (si lo usas) ==========
            composable(Routes.Otp) {
                OtpScreen(
                    onVerify = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ========== PRINCIPAL ==========
            composable(Routes.Home) {
                HomeScreen(
                    userViewModel = userViewModel,
                    onYapear = { navController.navigate(Routes.Search) },
                    onReceive = { navController.navigate(Routes.Receive) },
                    onHistory = { navController.navigate(Routes.History) }
                )
            }

            composable(Routes.History) {
                HistoryScreen()
            }

            // ========== PERFIL ==========
            composable(Routes.Profile) {
                ProfileScreen(
                    onOpenData = { navController.navigate(Routes.ProfileData) },
                    onChangePin = { navController.navigate(Routes.PinCurrent) },
                    onLogout = {
                        navController.navigate(Routes.Splash) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ProfileData) {
                ProfileDataScreen(
                    userViewModel = userViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // ========== CAMBIO DE PIN ==========
            composable(Routes.PinCurrent) {
                PinCurrentScreen(
                    onOk = { navController.navigate(Routes.PinNew) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.PinNew) {
                PinNewScreen(
                    onNext = { newPin ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("newPin", newPin)
                        navController.navigate(Routes.PinConfirm)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.PinConfirm) {
                val firstPin = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("newPin") ?: ""

                PinConfirmScreen(
                    firstPin = firstPin,
                    onOk = { navController.navigate(Routes.PinSuccess) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.PinSuccess) {
                PinSuccessScreen(
                    onHome = {
                        navController.navigate(Routes.Profile) {
                            popUpTo(Routes.Profile) { inclusive = true }
                        }
                    }
                )
            }

            // ========== ENVIAR DINERO ==========
            composable(Routes.Search) { backStackEntry ->
                // ⬅️ aquí va el remember
                val parent = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val vm: SendViewModel = viewModel(parent)
                SearchContactScreen(
                    vm = vm,
                    onPicked = { navController.navigate(Routes.Amount) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.Amount) { backStackEntry ->
                val parent = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val vm: SendViewModel = viewModel(parent)
                AmountScreen(
                    vm = vm,
                    onNext = { navController.navigate(Routes.SendConfirm) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SendConfirm) { backStackEntry ->
                // este sí lo seguimos sacando del scope "root"
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val sendVm: SendViewModel = viewModel(parentEntry)

                // OJO: acá NO creamos otro UserViewModel,
                // usamos el que ya declaraste arriba en AppNav()
                ConfirmPinScreen(
                    vm = sendVm,
                    userVm = userViewModel,   // <-- este es el bueno
                    onBack = { navController.popBackStack() },
                    onOk = {
                        // si todo salió bien en backend, vamos al éxito
                        navController.navigate(Routes.Success) {
                            popUpTo(Routes.Home) { inclusive = false }
                        }
                    }
                )
            }


            // ========== RECIBIR ==========
            composable(Routes.Receive) {
                ReceiveScreen(onBack = { navController.popBackStack() })
            }

            // ========== ÉXITO ==========
            composable(Routes.Success) { backStackEntry ->
                val parent = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val vm: SendViewModel = viewModel(parent)
                SuccessScreen(
                    alias = vm.contact?.alias ?: "-",
                    wallet = vm.contact?.wallet ?: "-",
                    amount = vm.amount,
                    onHome = {
                        vm.clear()
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
