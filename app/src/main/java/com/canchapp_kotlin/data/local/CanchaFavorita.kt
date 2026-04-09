package com.canchapp_kotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para una cancha guardada como favorita
 * Se almacena en la base de datos local Room
 */
@Entity(tableName = "canchas_favoritas")
data class CanchaFavorita(
    @PrimaryKey
    val complexId: String,              // ID único de la cancha
    val name: String,                   // Nombre del complejo
    val address: String,                // Dirección
    val city: String,                   // Ciudad
    val minPrice: Double,               // Precio mínimo por hora
    val fieldsCount: Int,               // Cantidad de canchas
    val distanceKm: Double? = null,     // Distancia desde la ubicación del usuario
    val timestamp: Long = System.currentTimeMillis()  // Fecha de guardado
)
