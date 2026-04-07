package com.canchapp_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.canchapp_kotlin.ui.theme.CanchappkotlinTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class AppScreen { REGISTER_LANDING, LOGIN, REGISTER_FORM, HOME }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanchappkotlinTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "register"   // Pantalla inicial
                ) {

                    composable("register") {
                        RegisterScreen(
                            onRegisterWithGoogle    = { /* TODO */ },
                            onRegisterWithInstagram = { /* TODO */ },
                            onRegisterWithTikTok    = { /* TODO */ },
                            onRegisterWithEmail     = { /* TODO */ },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    composable("login") {
                        LoginScreen(
                            onLoginWithGoogle    = { /* TODO */ },
                            onLoginWithInstagram = { /* TODO */ },
                            onLoginWithTikTok    = { /* TODO */ },
                            onLoginWithEmail = { navController.navigate("login_email") },
                            onRegisterClick  = { navController.navigate("register") }
                        )
                    }

                    composable("login_email") {
                        LoginEmailScreen(
                            onBack           = { navController.popBackStack() },
                            onForgotPassword = { /* TODO */ },
                            onRegisterClick  = { navController.navigate("register") },
                            onLogin = { email, password ->
                            }
                        )
                    }
                }
            }
        }
    }
}