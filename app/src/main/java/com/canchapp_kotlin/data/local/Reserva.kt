package com.canchapp_kotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val complexId: String,
    val complexName: String,
    val address: String,
    val city: String,
    val fecha: String,          // "2026-04-09"
    val hora: String,           // "10:00"
    val duracionHoras: Int,     // 1, 2...
    val precioTotal: Double,
    val timestamp: Long = System.currentTimeMillis()
)
