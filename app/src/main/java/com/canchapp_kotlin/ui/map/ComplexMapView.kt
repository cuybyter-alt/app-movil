package com.canchapp_kotlin.ui.map

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canchapp_kotlin.R
import com.canchapp_kotlin.data.network.ComplexDto
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

private val MvGreenAccent = Color(0xFF4CAF50)
private val MvNavDark     = Color(0xFF1A2218)
private val MvTextDark    = Color(0xFF1B1B1B)
private val MvTextGray    = Color(0xFF6B6B6B)

@Composable
fun ComplexMapView(
    complexes: List<ComplexDto>,
    userLat: Double?,
    userLon: Double?,
    modifier: Modifier = Modifier
) {
    val centerLat = userLat
        ?: if (complexes.isNotEmpty()) complexes.map { it.latitude }.average() else 2.4419
    val centerLon = userLon
        ?: if (complexes.isNotEmpty()) complexes.map { it.longitude }.average() else -76.5320

    var selectedComplex by remember { mutableStateOf<ComplexDto?>(null) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(centerLon, centerLat))
            zoom(12.0)
        }
    }

    Box(modifier = modifier) {
        MapboxMap(
            modifier         = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style            = { MapStyle(style = Style.DARK) }
        ) {
            MapEffect(complexes, userLat, userLon) { mapView ->
                // Show built-in blue location puck for the user
                mapView.location.updateSettings {
                    enabled = true
                }

                // Clear previous bubble annotations before re-adding
                mapView.viewAnnotationManager.removeAllViewAnnotations()

                // Add a bubble for each complex
                complexes.forEach { complex ->
                    val view = mapView.viewAnnotationManager.addViewAnnotation(
                        resId = R.layout.marker_complex,
                        options = viewAnnotationOptions {
                            geometry(Point.fromLngLat(complex.longitude, complex.latitude))
                            allowOverlap(true)
                        }
                    )
                    view.findViewById<TextView>(R.id.tvComplexName).text = complex.name
                    view.findViewById<TextView>(R.id.tvComplexPrice).text =
                        "$${(complex.minPrice / 1000).toInt()}k/h"
                    view.setOnClickListener { selectedComplex = complex }
                }
            }
        }

        // Re-center FAB
        SmallFloatingActionButton(
            onClick = {
                mapViewportState.flyTo(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(centerLon, centerLat))
                        .zoom(12.0)
                        .build()
                )
            },
            modifier       = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            containerColor = MvNavDark,
            contentColor   = MvGreenAccent
        ) {
            Icon(
                imageVector        = Icons.Default.MyLocation,
                contentDescription = "Centrar mapa",
                modifier           = Modifier.size(18.dp)
            )
        }

        // Info card when a complex bubble is tapped
        selectedComplex?.let { complex ->
            ComplexMapCard(
                complex   = complex,
                onDismiss = { selectedComplex = null },
                modifier  = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ComplexMapCard(
    complex: ComplexDto,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MvNavDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(Color(0xFF2D4A27), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint               = MvGreenAccent,
                    modifier           = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = complex.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint               = MvTextGray,
                        modifier           = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = complex.address, fontSize = 12.sp, color = MvTextGray)
                }
                if (complex.distanceKm != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text       = "${"%.1f".format(complex.distanceKm)} km · ${complex.fieldsCount} canchas",
                        fontSize   = 12.sp,
                        color      = MvGreenAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text       = "Desde \$${(complex.minPrice / 1000).toInt()}k /hr",
                    fontSize   = 13.sp,
                    color      = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = MvTextGray)
            }
        }
    }
}