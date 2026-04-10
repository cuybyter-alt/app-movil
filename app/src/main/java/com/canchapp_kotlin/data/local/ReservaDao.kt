package com.canchapp_kotlin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserva: Reserva)

    @Query("DELETE FROM reservas WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM reservas ORDER BY timestamp DESC")
    fun getAllFlow(): Flow<List<Reserva>>

    @Query("SELECT COUNT(*) FROM reservas WHERE complexId = :complexId")
    suspend fun countByComplexId(complexId: String): Int
}
