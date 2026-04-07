package com.canchapp_kotlin

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.ui.theme.CanchappkotlinTheme

// ─────────────────────────────────────────────────────────────
//  COLORES CUSTOM
// ─────────────────────────────────────────────────────────────
private val GreenPrimary    = Color(0xFF4CAF50) // Verde principal del botón "Correo"
private val GreenDark       = Color(0xFF388E3C) // Verde oscuro para degradado del fondo
private val GreenLight      = Color(0xFF81C784) // Verde claro para degradado del fondo
private val InstagramStart  = Color(0xFFF58529) // Naranja-instagram (inicio del gradiente)
private val InstagramEnd    = Color(0xFFDD2A7B) // Rosa-instagram (fin del gradiente)
private val TikTokBlack     = Color(0xFF010101) // Negro TikTok
private val DividerGray     = Color(0xFFBDBDBD) // Gris para la línea divisoria
private val LinkGreen       = Color(0xFF2E7D32) // Verde para el link "Inicia sesión"
private val CardBackground  = Color(0xFFFAFAFA) // Fondo de la tarjeta blanca
private val SubtitleGray    = Color(0xFF757575) // Gris para el subtítulo

// ─────────────────────────────────────────────────────────────
//  PANTALLA PRINCIPAL DE REGISTRO
//  Parámetros de callbacks para cada acción del usuario
// ─────────────────────────────────────────────────────────────
@Composable
fun RegisterScreen(
    onRegisterWithGoogle: () -> Unit = {},
    onRegisterWithInstagram: () -> Unit = {},
    onRegisterWithTikTok: () -> Unit = {},
    onRegisterWithEmail: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    // Fondo degradado verde que cubre toda la pantalla
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
        // Tarjeta blanca central con scroll por si el contenido no cabe en pantallas pequeñas
        Card(
            modifier = Modifier
                .fillMaxWidth(0.90f)           // 90% del ancho de pantalla
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
                    .verticalScroll(rememberScrollState()), // Permite scroll interno
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ── LOGO ──────────────────────────────────────────────
                // Reemplaza este Box por tu Image() cuando tengas el logo.
                // Ejemplo:
                //   Image(
                //       painter = painterResource(id = R.drawable.ic_logo),
                //       contentDescription = "Logo CanchApp",
                //       modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp))
                //   )
                LogoPlaceholder()

                Spacer(modifier = Modifier.height(4.dp))

                // ── TÍTULO ────────────────────────────────────────────
                Text(
                    text = "Crear Cuenta",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B1B1B)
                )

                // ── SUBTÍTULO ─────────────────────────────────────────
                Text(
                    text = "Únete a miles de jugadores encontrando su cancha perfecta",
                    fontSize = 14.sp,
                    color = SubtitleGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ── BOTÓN GOOGLE ──────────────────────────────────────
                // Borde gris, fondo blanco, texto negro → estilo oficial de Google
                GradientButton(
                    text = "Registrarse con Google",
                    iconRes = R.drawable.google,
                    gradient = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4285F4), Color(0xFF34A853)) // azul→verde Google
                    ),
                    onClick = onRegisterWithGoogle
                )

                // ── BOTÓN INSTAGRAM ───────────────────────────────────
                // Fondo degradado naranja→rosa, estilo Instagram
                GradientButton(
                    text = "Registrarse con Instagram",
                    iconRes = R.drawable.instagram,
                    gradient = Brush.horizontalGradient(
                        colors = listOf(InstagramStart, InstagramEnd)
                    ),
                    onClick = onRegisterWithInstagram
                )

                // ── BOTÓN TIKTOK ──────────────────────────────────────
                // Fondo negro, texto blanco
                SocialButton(
                    text = "Registrarse con TikTok",
                    iconContent = { TikTokIcon() },
                    onClick = onRegisterWithTikTok,
                    textColor = Color.White,
                    containerColor = TikTokBlack
                )

                // ── SEPARADOR "o" ─────────────────────────────────────
                OrDivider()

                // ── BOTÓN CORREO ──────────────────────────────────────
                // Fondo verde principal, ícono de sobre
                SocialButton(
                    text = "Registrarse con Correo",
                    iconContent = { EmailIcon() },
                    onClick = onRegisterWithEmail,
                    textColor = Color.White,
                    containerColor = GreenPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── PIE: "¿Ya tienes cuenta? Inicia sesión" ───────────
                LoginFooter(onLoginClick = onLoginClick)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Placeholder del Logo
//  Cuando tengas el logo, reemplaza este composable en RegisterScreen
// ─────────────────────────────────────────────────────────────
@Composable
private fun LogoPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.cuypequeniologo),
        contentDescription = "Logo CanchApp",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(18.dp))
    )
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Botón Social genérico (Google, TikTok, Correo)
//  Acepta cualquier composable como ícono y colores configurables
// ─────────────────────────────────────────────────────────────
@Composable
private fun SocialButton(
    text: String,
    iconContent: @Composable () -> Unit,    // Ícono flexible (puede ser Image, Text, Icon...)
    onClick: () -> Unit,
    textColor: Color,
    containerColor: Color,
    border: BorderStroke? = null            // Opcional: borde para Google
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = border,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = textColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            iconContent()                              // Renderiza el ícono pasado
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Botón con fondo degradado (Instagram)
//  Usa Canvas/Box con gradient porque Button no soporta degradado nativo
// ─────────────────────────────────────────────────────────────
@Composable
private fun GradientButton(
    text: String,
    iconRes: Int,                           // Resource ID del drawable
    gradient: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = gradient)   // El degradado se aplica aquí en el Box
    ) {
        // Usamos Button transparente encima para capturar el click y mostrar ripple
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Separador "o" entre botones sociales y correo
// ─────────────────────────────────────────────────────────────
@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = DividerGray
        )
        Text(
            text = "  o  ",
            color = DividerGray,
            fontSize = 14.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = DividerGray
        )
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTE: Pie de pantalla con link a Login
// ─────────────────────────────────────────────────────────────
@Composable
private fun LoginFooter(onLoginClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "¿Ya tienes una cuenta? ",
            fontSize = 14.sp,
            color = SubtitleGray
        )
        TextButton(
            onClick = onLoginClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Inicia sesión",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LinkGreen
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  ÍCONOS PLACEHOLDER EN TEXTO/EMOJI
//  Reemplázalos con Icon(painterResource(R.drawable.ic_xxx))
//  cuando tengas tus drawables SVG
// ─────────────────────────────────────────────────────────────

/** Placeholder ícono TikTok — reemplaza con drawable real */
@Composable
private fun TikTokIcon() {
    Icon(painter = painterResource(R.drawable.tiktok), contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
}

/** Ícono de sobre para el botón de correo */
@Composable
private fun EmailIcon() {
    Text(text = "✉", fontSize = 18.sp, color = Color.White)
    // Alternativa con Material Icons (ya incluido en Material3):
    // Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
}

// ─────────────────────────────────────────────────────────────
//  PREVIEWS — Se ven en el panel "Design" de Android Studio
// ─────────────────────────────────────────────────────────────

/** Preview completa en modo claro (tema por defecto) */
@Preview(
    name = "Registro - Pantalla completa",
    showBackground = true,
    showSystemUi = true,   // Muestra la barra de estado y navegación
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
fun RegisterScreenPreview() {
    CanchappkotlinTheme {
        RegisterScreen()
    }
}

/** Preview solo de la tarjeta, sin fondo verde */
@Preview(
    name = "Registro - Solo tarjeta",
    showBackground = true,
    backgroundColor = 0xFF4CAF50   // Fondo verde para que la tarjeta destaque
)
@Composable
fun RegisterCardPreview() {
    CanchappkotlinTheme {
        RegisterScreen()
    }
}

/** Preview en pantalla pequeña (API mínima ~360x640) */
@Preview(
    name = "Registro - Pantalla pequeña",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=320dp,height=640dp,dpi=320"
)
@Composable
fun RegisterScreenSmallPreview() {
    CanchappkotlinTheme {
        RegisterScreen()
    }
}

/** Preview en modo oscuro — el tema gestiona los colores automáticamente */
@Preview(
    name = "Registro - Modo oscuro",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RegisterScreenDarkPreview() {
    CanchappkotlinTheme(darkTheme = true) {
        RegisterScreen()
    }
}