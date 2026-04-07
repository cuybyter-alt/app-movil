package com.canchapp_kotlin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.ui.auth.AuthViewModel
import com.canchapp_kotlin.util.Resource

private val LGreenPrimary   = Color(0xFF4CAF50)
private val LGreenDark      = Color(0xFF388E3C)
private val LGreenLight     = Color(0xFF81C784)
private val LLinkGreen      = Color(0xFF2E7D32)
private val LCardBackground = Color(0xFFFAFAFA)
private val LSubtitleGray   = Color(0xFF757575)
private val LTextDark       = Color(0xFF1B1B1B)

@Composable
private fun loginInputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor           = LTextDark,
    unfocusedTextColor         = LTextDark,
    focusedLabelColor          = LGreenPrimary,
    unfocusedLabelColor        = LSubtitleGray,
    focusedBorderColor         = LGreenPrimary,
    unfocusedBorderColor       = Color(0xFFBDBDBD),
    cursorColor                = LGreenPrimary,
    focusedTrailingIconColor   = LSubtitleGray,
    unfocusedTrailingIconColor = LSubtitleGray
)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val loginState by authViewModel.loginState.collectAsState()

    var identifier      by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading    = loginState is Resource.Loading
    val isSuccess    = loginState is Resource.Success
    val errorMessage = (loginState as? Resource.Error)?.message

    LaunchedEffect(loginState) {
        if (loginState is Resource.Success) {
            kotlinx.coroutines.delay(1200)
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LGreenLight, LGreenDark))),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .wrapContentHeight()
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LCardBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cuypequeniologo),
                    contentDescription = "Logo CanchApp",
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(18.dp))
                )

                Text(
                    text = "Iniciar Sesion",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LTextDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Ingresa tus datos para continuar",
                    fontSize = 14.sp,
                    color = LSubtitleGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                when {
                    isSuccess -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "✓", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "Sesion iniciada correctamente!",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    isLoading -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = LGreenPrimary,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "Verificando credenciales...",
                                    color = Color(0xFF2E7D32),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    errorMessage != null -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage,
                                color = Color(0xFFB71C1C),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it; authViewModel.resetLoginState() },
                    label = { Text("Usuario o correo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = loginInputColors()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; authViewModel.resetLoginState() },
                    label = { Text("Contrasena") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = loginInputColors(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility
                                              else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        authViewModel.resetLoginState()
                        authViewModel.login(identifier.trim(), password)
                    },
                    enabled = !isLoading && !isSuccess && identifier.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LGreenPrimary)
                ) {
                    Text(text = "Iniciar Sesion", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("No tienes cuenta? ", fontSize = 14.sp, color = LSubtitleGray)
                    TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            text = "Registrate",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LLinkGreen
                        )
                    }
                }
            }
        }
    }
}