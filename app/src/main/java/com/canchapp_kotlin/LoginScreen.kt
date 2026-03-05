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
//  COLORES CUSTOM  (mismos que RegisterScreen para consistencia)
// ─────────────────────────────────────────────────────────────
private val GreenPrimary   = Color(0xFF4CAF50)
private val GreenDark      = Color(0xFF388E3C)
private val GreenLight     = Color(0xFF81C784)
private val InstagramStart = Color(0xFFF58529)
private val InstagramEnd   = Color(0xFFDD2A7B)
private val TikTokBlack    = Color(0xFF010101)
private val DividerGray    = Color(0xFFBDBDBD)
private val LinkGreen      = Color(0xFF2E7D32)
private val CardBackground = Color(0xFFFAFAFA)
private val SubtitleGray   = Color(0xFF757575)

// ─────────────────────────────────────────────────────────────
//  PANTALLA PRINCIPAL DE LOGIN
// ─────────────────────────────────────────────────────────────
@Composable
fun LoginScreen(
    onLoginWithGoogle: () -> Unit = {},
    onLoginWithInstagram: () -> Unit = {},
    onLoginWithTikTok: () -> Unit = {},
    onLoginWithEmail: () -> Unit = {},   // Navega a LoginEmailScreen
    onRegisterClick: () -> Unit = {}
) {
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

                Spacer(modifier = Modifier.height(8.dp))

                // ── BOTÓN GOOGLE ──────────────────────────────────────
                LoginGradientButton(
                    text = "Continuar con Google",
                    iconRes = R.drawable.google,
                    gradient = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4285F4), Color(0xFF34A853))
                    ),
                    onClick = onLoginWithGoogle
                )

                // ── BOTÓN INSTAGRAM ───────────────────────────────────
                LoginGradientButton(
                    text = "Continuar con Instagram",
                    iconRes = R.drawable.instagram,
                    gradient = Brush.horizontalGradient(
                        colors = listOf(InstagramStart, InstagramEnd)
                    ),
                    onClick = onLoginWithInstagram
                )

                // ── BOTÓN TIKTOK ──────────────────────────────────────
                LoginSocialButton(
                    text = "Continuar con TikTok",
                    iconContent = {
                        Icon(
                            painter = painterResource(R.drawable.tiktok),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    onClick = onLoginWithTikTok,
                    textColor = Color.White,
                    containerColor = TikTokBlack
                )

                // ── SEPARADOR "o" ─────────────────────────────────────
                LoginOrDivider()

                // ── BOTÓN CORREO ──────────────────────────────────────
                LoginSocialButton(
                    text = "Continuar con Correo",
                    iconContent = {
                        Text(text = "✉", fontSize = 18.sp, color = Color.White)
                    },
                    onClick = onLoginWithEmail,
                    textColor = Color.White,
                    containerColor = GreenPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ── PIE: "¿No tienes una cuenta? Regístrate" ──────────
                RegisterFooter(onRegisterClick = onRegisterClick)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  COMPONENTES PRIVADOS
// ─────────────────────────────────────────────────────────────

@Composable
private fun LoginSocialButton(
    text: String,
    iconContent: @Composable () -> Unit,
    onClick: () -> Unit,
    textColor: Color,
    containerColor: Color,
    border: BorderStroke? = null
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
            iconContent()
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

@Composable
private fun LoginGradientButton(
    text: String,
    iconRes: Int,
    gradient: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = gradient)
    ) {
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

@Composable
private fun LoginOrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = DividerGray)
        Text(text = "  o  ", color = DividerGray, fontSize = 14.sp)
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = DividerGray)
    }
}

@Composable
private fun RegisterFooter(onRegisterClick: () -> Unit) {
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

// ─────────────────────────────────────────────────────────────
//  PREVIEWS
// ─────────────────────────────────────────────────────────────

@Preview(
    name = "Login - Pantalla completa",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
fun LoginScreenPreview() {
    CanchappkotlinTheme { LoginScreen() }
}

@Preview(
    name = "Login - Pantalla pequeña",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=320dp,height=640dp,dpi=320"
)
@Composable
fun LoginScreenSmallPreview() {
    CanchappkotlinTheme { LoginScreen() }
}

@Preview(
    name = "Login - Modo oscuro",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenDarkPreview() {
    CanchappkotlinTheme(darkTheme = true) { LoginScreen() }
}