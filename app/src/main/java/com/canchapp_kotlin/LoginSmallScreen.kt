package com.canchapp_kotlin

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.ui.theme.CanchappkotlinTheme

// ─────────────────────────────────────────────────────────────
//  COLORES CUSTOM  (consistentes con LoginScreen y RegisterScreen)
// ─────────────────────────────────────────────────────────────
private val GreenPrimary    = Color(0xFF4CAF50)
private val GreenDark       = Color(0xFF388E3C)
private val GreenLight      = Color(0xFF81C784)
private val LinkGreen       = Color(0xFF2E7D32)
private val CardBackground  = Color(0xFFFAFAFA)
private val SubtitleGray    = Color(0xFF757575)
private val FieldBorder     = Color(0xFFBDBDBD)
private val FieldBackground = Color(0xFFFFFFFF)
private val ErrorRed        = Color(0xFFD32F2F)

// ─────────────────────────────────────────────────────────────
//  PANTALLA DE LOGIN CON CORREO Y CONTRASEÑA
// ─────────────────────────────────────────────────────────────
@Composable
fun LoginEmailScreen(
    onBack: () -> Unit = {},               // Volver a LoginScreen
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    // Estado del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados de error simples (validación básica en el cliente)
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    // Fondo degradado verde idéntico a las otras pantallas
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenLight, GreenDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .wrapContentHeight()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ── LOGO ──────────────────────────────────────────────
                Image(
                    painter = painterResource(id = R.drawable.cuypequeniologo),
                    contentDescription = "Logo CanchApp",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(18.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── TÍTULO ────────────────────────────────────────────
                Text(
                    text = "Bienvenido de Nuevo",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B1B1B),
                    textAlign = TextAlign.Center
                )

                // ── SUBTÍTULO ─────────────────────────────────────────
                Text(
                    text = "Inicia sesión para reservar tus canchas favoritas",
                    fontSize = 14.sp,
                    color = SubtitleGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── CAMPO: CORREO ELECTRÓNICO ─────────────────────────
                EmailField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null   // Limpiar error al escribir
                    },
                    error = emailError,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )

                // ── CAMPO: CONTRASEÑA ─────────────────────────────────
                PasswordField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    visible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible },
                    error = passwordError,
                    onDone = {
                        focusManager.clearFocus()
                        handleLogin(email, password, { emailError = it }, { passwordError = it }, onLogin)
                    }
                )

                // ── FILA: VOLVER | ¿OLVIDASTE TU CONTRASEÑA? ─────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón "← Volver"
                    TextButton(
                        onClick = onBack,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = LinkGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Volver",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LinkGreen
                        )
                    }

                    // Botón "¿Olvidaste tu contraseña?"
                    TextButton(
                        onClick = onForgotPassword,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 13.sp,
                            color = SubtitleGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── BOTÓN INGRESAR ────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush = Brush.horizontalGradient(
                            colors = listOf(GreenPrimary, GreenDark)
                        ))
                ) {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            handleLogin(email, password, { emailError = it }, { passwordError = it }, onLogin)
                        },
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = "Ingresar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── PIE: "¿No tienes una cuenta? Regístrate" ──────────
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿No tienes una cuenta? ", fontSize = 14.sp, color = SubtitleGray)
                    TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            text = "Regístrate",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LinkGreen
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Campo de correo electrónico
// ─────────────────────────────────────────────────────────────
@Composable
private fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Correo Electrónico",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B1B1B)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "tu@correo.com", color = FieldBorder)
            },
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { onNext() }),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = FieldBorder,
                errorBorderColor = ErrorRed,
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground
            )
        )
        if (error != null) {
            Text(text = error, color = ErrorRed, fontSize = 12.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Campo de contraseña con toggle de visibilidad
// ─────────────────────────────────────────────────────────────
@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    error: String?,
    onDone: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Contraseña",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1B1B1B)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "••••••••", color = FieldBorder)
            },
            singleLine = true,
            isError = error != null,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = SubtitleGray
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = FieldBorder,
                errorBorderColor = ErrorRed,
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground
            )
        )
        if (error != null) {
            Text(text = error, color = ErrorRed, fontSize = 12.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  FUNCIÓN: Validación básica del formulario antes de llamar al
//  callback onLogin (la lógica real de autenticación va en el ViewModel)
// ─────────────────────────────────────────────────────────────
private fun handleLogin(
    email: String,
    password: String,
    onEmailError: (String) -> Unit,
    onPasswordError: (String) -> Unit,
    onLogin: (String, String) -> Unit
) {
    var valid = true

    if (email.isBlank()) {
        onEmailError("El correo no puede estar vacío")
        valid = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onEmailError("Ingresa un correo válido")
        valid = false
    }

    if (password.isBlank()) {
        onPasswordError("La contraseña no puede estar vacía")
        valid = false
    } else if (password.length < 6) {
        onPasswordError("Mínimo 6 caracteres")
        valid = false
    }

    if (valid) onLogin(email, password)
}

// ─────────────────────────────────────────────────────────────
//  PREVIEWS
// ─────────────────────────────────────────────────────────────

@Preview(
    name = "Login Email - Pantalla completa",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
fun LoginEmailScreenPreview() {
    CanchappkotlinTheme { LoginEmailScreen() }
}

@Preview(
    name = "Login Email - Pantalla pequeña",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=320dp,height=640dp,dpi=320"
)
@Composable
fun LoginEmailScreenSmallPreview() {
    CanchappkotlinTheme { LoginEmailScreen() }
}

@Preview(
    name = "Login Email - Modo oscuro",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginEmailScreenDarkPreview() {
    CanchappkotlinTheme(darkTheme = true) { LoginEmailScreen() }
}