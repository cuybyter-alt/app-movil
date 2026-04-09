package com.canchapp_kotlin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para todas las operaciones con la tabla de favoritos
 * Proporciona métodos para INSERT, UPDATE, DELETE y SELECT
 */
@Dao
interface CanchaFavoritaDao {
    
    /**
     * Inserta una cancha a favoritos.
     * Si ya existe el ID, la reemplaza (REPLACE strategy)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorita(cancha: CanchaFavorita)
    
    /**
     * Elimina una cancha de favoritos
     */
    @Delete
    suspend fun deleteFavorita(cancha: CanchaFavorita)
    
    /**
     * Obtiene TODOS los favoritos ordenados por más reciente primero
     * Retorna un Flow<List> para actualizaciones en tiempo real
     */
    @Query("SELECT * FROM canchas_favoritas ORDER BY timestamp DESC")
    fun getAllFavoritas(): Flow<List<CanchaFavorita>>
    
    /**
     * Busca una cancha favorita específica por su ID
     * Retorna null si no existe
     */
    @Query("SELECT * FROM canchas_favoritas WHERE complexId = :complexId LIMIT 1")
    suspend fun getFavoritaById(complexId: String): CanchaFavorita?
    
    /**
     * Elimina una cancha favorita por su ID
     */
    @Query("DELETE FROM canchas_favoritas WHERE complexId = :complexId")
    suspend fun deleteFavoritaById(complexId: String)
    
    /**
     * Obtiene el total de favoritos
     * Retorna un Flow<Int> para actualizaciones en tiempo real
     */
    @Query("SELECT COUNT(*) FROM canchas_favoritas")
    fun getTotalFavoritas(): Flow<Int>
}
