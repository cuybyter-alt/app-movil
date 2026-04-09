package com.canchapp_kotlin.data.repository

import com.canchapp_kotlin.data.local.CanchaFavorita
import com.canchapp_kotlin.data.local.CanchaFavoritaDao
import kotlinx.coroutines.flow.Flow

/**
 * Capa intermedia entre DAO y ViewModel
 * Abstrae la lógica de acceso a datos
 * Facilita testing y mantenimiento
 */
class CanchaFavoritaRepository(private val dao: CanchaFavoritaDao) {
    
    /**
     * Obtiene todas las canchas favoritas en tiempo real
     */
    fun getAllFavoritas(): Flow<List<CanchaFavorita>> = dao.getAllFavoritas()
    
    /**
     * Obtiene el total de favoritos en tiempo real
     */
    fun getTotalFavoritas(): Flow<Int> = dao.getTotalFavoritas()
    
    /**
     * Verifica si una cancha específica está en favoritos
     */
    suspend fun isFavorita(complexId: String): Boolean {
        return dao.getFavoritaById(complexId) != null
    }
    
    /**
     * Agrega una cancha a favoritos
     */
    suspend fun addFavorita(cancha: CanchaFavorita) {
        dao.insertFavorita(cancha)
    }
    
    /**
     * Elimina una cancha de favoritos
     */
    suspend fun removeFavorita(complexId: String) {
        dao.deleteFavoritaById(complexId)
    }
}
