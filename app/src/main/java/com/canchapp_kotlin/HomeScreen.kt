package com.canchapp_kotlin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canchapp_kotlin.data.network.ComplexDto
import com.canchapp_kotlin.data.network.UserDto
import com.canchapp_kotlin.ui.complex.ComplexViewModel
import com.canchapp_kotlin.ui.components.TopNavBar
import com.canchapp_kotlin.util.Resource
import kotlinx.coroutines.launch

private val HWhiteBg       = Color(0xFFF5F5F5)
private val HGreenAccent   = Color(0xFF4CAF50)
private val HCardDarkGreen = Color(0xFF2D4A27)
private val HNavDark       = Color(0xFF1A2218)
private val HTextDark      = Color(0xFF1B1B1B)
private val HTextGray      = Color(0xFF6B6B6B)
private val HDrawerBg      = Color(0xFFFFFFFF)
private val HDrawerHeader  = Color(0xFF1A2218)

// ── DRAWER ─────────────────────────────────────────────────────────────────

private data class DrawerItem(
    val icon: ImageVector,
    val label: String,
    val isLogout: Boolean = false
)

private val drawerItems = listOf(
    DrawerItem(Icons.Default.Home,       "Inicio"),
    DrawerItem(Icons.Default.Search,     "Buscar Canchas"),
    DrawerItem(Icons.Default.BookOnline, "Mis Reservas"),
    DrawerItem(Icons.Default.Favorite,   "Favoritos")
)

@Composable
private fun AppDrawerContent(
    user: UserDto?,
    onClose: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(HDrawerBg)
    ) {
        // ── HEADER ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HDrawerHeader)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cuypequeniologo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CanchApp",
                    color = HGreenAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }

        // ── USER INFO ──────────────────────────────────────────────────
        if (user != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(HGreenAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cuypequeniologo),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = HTextDark
                    )
                    Text(
                        text = user.email,
                        fontSize = 12.sp,
                        color = HTextGray,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
            HorizontalDivider(color = Color(0xFFE0E0E0))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── MENU ITEMS ─────────────────────────────────────────────────
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(item.icon, contentDescription = item.label, tint = HTextDark)
                },
                label = {
                    Text(item.label, fontWeight = FontWeight.Medium, color = HTextDark)
                },
                selected = false,
                onClick = { /* TODO: navigate */ },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Mi Perfil
        NavigationDrawerItem(
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Mi Perfil", tint = HTextDark)
            },
            label = {
                Text("Mi Perfil", fontWeight = FontWeight.Medium, color = HTextDark)
            },
            selected = false,
            onClick = { /* TODO */ },
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // ── CERRAR SESIÓN ──────────────────────────────────────────────
        NavigationDrawerItem(
            icon = {
                Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión", tint = Color(0xFFE53935))
            },
            label = {
                Text("Cerrar Sesión", fontWeight = FontWeight.SemiBold, color = Color(0xFFE53935))
            },
            selected = false,
            onClick = onLogout,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )
    }
}

// ── HOME SCREEN ─────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    user: UserDto?,
    onLogout: () -> Unit,
    complexViewModel: ComplexViewModel = viewModel()
) {
    val complexesState by complexViewModel.complexesState.collectAsState()
    val drawerState    = rememberDrawerState(DrawerValue.Closed)
    val scope          = rememberCoroutineScope()
    var searchQuery    by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                user     = user,
                onClose  = { scope.launch { drawerState.close() } },
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HWhiteBg)
        ) {
            // ── TOP NAV BAR ────────────────────────────────────────────
            TopNavBar(
                onMenuClick    = { scope.launch { drawerState.open() } },
                onSearchClick  = {}
            )

            // ── CONTENT ────────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = HGreenAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Complejos Cercanos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = HTextDark
                            )
                        }
                        TextButton(onClick = { complexViewModel.loadComplexes() }) {
                            Text(
                                text = "Ver todos \u2192",
                                color = HGreenAccent,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // ── SEARCH BAR ─────────────────────────────────────────
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar complejo...", color = HTextGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = HTextGray)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = HTextGray)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = HGreenAccent,
                            unfocusedBorderColor = Color(0xFFDDDDDD),
                            focusedTextColor     = HTextDark,
                            unfocusedTextColor   = HTextDark,
                            cursorColor          = HGreenAccent
                        )
                    )
                }

                // State-driven content
                when (val state = complexesState) {
                    is Resource.Loading -> item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = HGreenAccent)
                        }
                    }

                    is Resource.Success -> {
                        val allItems = state.data.data?.items ?: emptyList()
                        val filtered = if (searchQuery.isBlank()) allItems
                        else allItems.filter { it.name.contains(searchQuery.trim(), ignoreCase = true) }

                        if (filtered.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (searchQuery.isBlank()) "No hay complejos disponibles"
                                        else "Sin resultados para \"$searchQuery\"",
                                        color = HTextGray,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        } else {
                            items(filtered) { complex ->
                                ComplexCard(complex = complex)
                            }
                        }
                    }

                    is Resource.Error -> item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = state.message,
                                    color = Color(0xFFB71C1C),
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { complexViewModel.loadComplexes() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Reintentar", tint = Color(0xFFB71C1C))
                                }
                            }
                        }
                    }

                    null -> {}
                }
            }
        }
    }
}

@Composable
private fun ComplexCard(complex: ComplexDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        HCardDarkGreen,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Apartment,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier
                        .size(96.dp)
                        .align(Alignment.Center)
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = HNavDark
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (complex.distanceKm != null) "${"%.1f".format(complex.distanceKm)} km" else "N/A",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(shape = RoundedCornerShape(20.dp), color = HNavDark) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(complex.city, color = Color.White, fontSize = 10.sp)
                        }
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = HNavDark) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("${complex.fieldsCount} canchas", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = complex.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = HTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = complex.address, color = HTextGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Desde", color = HTextGray, fontSize = 11.sp)
                        Text(
                            text = "\$${(complex.minPrice / 1000).toInt()}k /hr",
                            color = HTextDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HGreenAccent),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text("Ver canchas \u2192", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}