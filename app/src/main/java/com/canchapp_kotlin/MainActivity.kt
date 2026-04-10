package com.canchapp_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.activity.compose.BackHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canchapp_kotlin.ui.auth.AuthViewModel
import com.canchapp_kotlin.ui.favorites.FavoritesScreen
import com.canchapp_kotlin.ui.reservas.MisReservasScreen
import com.canchapp_kotlin.ui.splash.SplashScreen
import com.canchapp_kotlin.ui.theme.CanchappkotlinTheme

enum class AppScreen { SPLASH, REGISTER_LANDING, LOGIN, REGISTER_FORM, HOME, FAVORITES, RESERVAS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanchappkotlinTheme {
                val authViewModel: AuthViewModel = viewModel()
                val loggedUser by authViewModel.loggedUser.collectAsState()
                var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

                // Intercepta el botón de atrás del sistema para navegar en el state machine
                BackHandler(
                    enabled = currentScreen != AppScreen.REGISTER_LANDING && currentScreen != AppScreen.HOME
                ) {
                    currentScreen = when (currentScreen) {
                        AppScreen.SPLASH        -> AppScreen.SPLASH
                        AppScreen.LOGIN         -> AppScreen.REGISTER_LANDING
                        AppScreen.REGISTER_FORM -> AppScreen.REGISTER_LANDING
                        AppScreen.FAVORITES     -> AppScreen.HOME
                        AppScreen.RESERVAS      -> AppScreen.HOME
                        else                    -> AppScreen.REGISTER_LANDING
                    }
                }

                when (currentScreen) {
                    AppScreen.SPLASH -> SplashScreen(
                        onDismiss = { currentScreen = AppScreen.REGISTER_LANDING }
                    )
                    AppScreen.REGISTER_LANDING -> RegisterScreen(
                        onRegisterWithEmail = { currentScreen = AppScreen.REGISTER_FORM },
                        onLoginClick        = { currentScreen = AppScreen.LOGIN }
                    )
                    AppScreen.LOGIN -> LoginScreen(
                        onLoginSuccess  = { currentScreen = AppScreen.HOME },
                        onRegisterClick = { currentScreen = AppScreen.REGISTER_LANDING },
                        authViewModel   = authViewModel
                    )
                    AppScreen.REGISTER_FORM -> RegisterFormScreen(
                        onRegisterSuccess = { currentScreen = AppScreen.LOGIN },
                        onLoginClick      = { currentScreen = AppScreen.LOGIN },
                        authViewModel     = authViewModel
                    )
                    AppScreen.HOME -> HomeScreen(
                        user     = loggedUser,
                        onLogout = {
                            authViewModel.logout()
                            currentScreen = AppScreen.LOGIN
                        },
                        onNavigateToFavorites = { currentScreen = AppScreen.FAVORITES },
                        onNavigateToReservas  = { currentScreen = AppScreen.RESERVAS }
                    )
                    AppScreen.FAVORITES -> FavoritesScreen(
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                    AppScreen.RESERVAS -> MisReservasScreen(
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
            }
        }
    }
}