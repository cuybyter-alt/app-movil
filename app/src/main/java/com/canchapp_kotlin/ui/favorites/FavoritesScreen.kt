package com.canchapp_kotlin.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canchapp_kotlin.data.local.CanchaFavorita

private val HWhiteBg       = Color(0xFFF5F5F5)
private val HGreenAccent   = Color(0xFF4CAF50)
private val HCardDarkGreen = Color(0xFF2D4A27)
private val HNavDark       = Color(0xFF1A2218)
private val HTextDark      = Color(0xFF1B1B1B)
private val HTextGray      = Color(0xFF6B6B6B)

/**
 * Pantalla principal de Favoritos
 * Muestra una lista de canchas guardadas como favoritos
 * Permite eliminarlas con un clic en el corazón
 */
@Composable
fun FavoritesScreen(
    onBack: () -> Unit = {},
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoritas by viewModel.favoritas.collectAsState()
    val totalFavoritas by viewModel.totalFavoritas.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HWhiteBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HNavDark)
                .statusBarsPadding()
                .padding(vertical = 16.dp),
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                text = "Mis Favoritos",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (favoritas.isEmpty()) {
            EmptyFavoritesState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "$totalFavoritas favorita${if (totalFavoritas != 1) "s" else ""}",
                        color = HTextGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(favoritas) { cancha ->
                    FavoritoCanchaCard(
                        cancha = cancha,
                        onRemove = { viewModel.removeFavorita(cancha.complexId) }
                    )
                }
            }
        }
    }
}

/**
 * Composable para el estado vacío (sin favoritos)
 */
@Composable
private fun EmptyFavoritesState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = HTextGray,
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No tienes favoritos aún",
            color = HTextDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agrega canchas a favoritos para verlas aquí",
            color = HTextGray,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

/**
 * Card individual para una cancha favorita
 * Muestra todos los detalles y botón para eliminar
 */
@Composable
private fun FavoritoCanchaCard(
    cancha: CanchaFavorita,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HCardDarkGreen)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cancha.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Quitar de favoritos",
                        tint = HGreenAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ",
                        fontSize = 14.sp
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cancha.address,
                            color = HTextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = cancha.city,
                            color = HTextGray,
                            fontSize = 11.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚽ ",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${cancha.fieldsCount} cancha${if (cancha.fieldsCount != 1) "s" else ""}",
                        color = HTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Precio y distancia
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Precio desde
                    Column {
                        Text(
                            text = "Precio desde",
                            color = HTextGray,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "\$${(cancha.minPrice / 1000).toInt()}k /hr",
                            color = HGreenAccent,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    
                    // Distancia (si existe)
                    if (cancha.distanceKm != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF0F0F0)
                        ) {
                            Text(
                                text = "${"%.1f".format(cancha.distanceKm)} km",
                                color = HTextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
