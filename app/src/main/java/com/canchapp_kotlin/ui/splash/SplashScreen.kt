package com.canchapp_kotlin.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.R

private val SBgTop    = Color(0xFF1A2218)
private val SBgBot    = Color(0xFF0F1A0D)
private val SGreen    = Color(0xFF4CAF50)
private val SLightGreen = Color(0xFF81C784)
private val SWhite    = Color.White
private val SMuted    = Color(0xFFB0BEC5)

@Composable
fun SplashScreen(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(SBgTop, SBgBot))
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(SGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cuypequeniologo),
                    contentDescription = "CanchApp Logo",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "CanchApp",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SGreen,
                letterSpacing = 1.sp
            )
            Text(
                text = "Reservá tu cancha favorita",
                fontSize = 14.sp,
                color = SLightGreen,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(32.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SWhite.copy(alpha = 0.06f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Una plataforma diseñada para conectar a los amantes del fútbol con los mejores complejos deportivos. " +
                           "Explorá canchas cercanas, guardarlas como favoritas y reservá tu turno en segundos.",
                    fontSize = 13.sp,
                    color = SMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }

            Spacer(Modifier.height(36.dp))

            // ── Créditos ───────────────────────────────────────────────
            HorizontalDivider(
                color = SGreen.copy(alpha = 0.25f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Desarrollado con amor por",
                fontSize = 12.sp,
                color = SMuted,
                fontStyle = FontStyle.Italic
            )
            Spacer(Modifier.height(10.dp))

            CreditBadge("Jorge Martínez")
            Spacer(Modifier.height(8.dp))
            CreditBadge("Julián Muñoz")

            Spacer(Modifier.height(36.dp))

            // ── Toque para continuar ───────────────────────────────────
            Text(
                text = "Tocá para continuar",
                fontSize = 12.sp,
                color = SMuted.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun CreditBadge(name: String) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = SGreen.copy(alpha = 0.15f)
    ) {
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = SLightGreen,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}
