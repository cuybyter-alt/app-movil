package com.canchapp_kotlin.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.R

private val NavBg     = Color(0xFF1A2218)
private val NavAccent = Color(0xFF4CAF50)

@Composable
fun TopNavBar(
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLocationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavBg)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // ── HAMBURGER ──────────────────────────────────────────────────
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }

        // ── LOGO + APP NAME ────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.cuypequeniologo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CanchApp",
                color = NavAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // ── ACTION ICONS ───────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
            }
            IconButton(onClick = onLocationClick) {
                Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = Color.White)
            }
            Box {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-10).dp, y = 10.dp)
                )
            }
        }
    }
}