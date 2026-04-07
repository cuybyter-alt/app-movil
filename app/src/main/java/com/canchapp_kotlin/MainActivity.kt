package com.canchapp_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canchapp_kotlin.ui.auth.AuthViewModel
import com.canchapp_kotlin.ui.theme.CanchappkotlinTheme

enum class AppScreen { REGISTER_LANDING, LOGIN, REGISTER_FORM, HOME }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanchappkotlinTheme {
                val authViewModel: AuthViewModel = viewModel()
                val loggedUser by authViewModel.loggedUser.collectAsState()
                var currentScreen by remember { mutableStateOf(AppScreen.REGISTER_LANDING) }

                when (currentScreen) {
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
                        }
                    )
                }
            }
        }
    }
}