package com.canchapp_kotlin.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canchapp_kotlin.data.local.CanchaFavorita
import com.canchapp_kotlin.data.local.CanchAppDatabase
import com.canchapp_kotlin.data.repository.CanchaFavoritaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar los datos de favoritos
 * - Maneja el ciclo de vida de la aplicación
 * - Expone los datos como StateFlow (compatible con Compose)
 * - Maneja las coroutines de forma segura
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    
    // Inicializa la BD y el repository
    private val database = CanchAppDatabase.getDatabase(application)
    private val repository = CanchaFavoritaRepository(database.canchaFavoritaDao())
    
    /**
     * Lista de todas las canchas favoritas
     * Se actualiza en tiempo real
     */
    val favoritas: StateFlow<List<CanchaFavorita>> = repository.getAllFavoritas()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    
    /**
     * Total de canchas favoritas
     * Se actualiza automáticamente
     */
    val totalFavoritas: StateFlow<Int> = repository.getTotalFavoritas()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )
    
    /**
     * Agrega una cancha a favoritos
     */
    fun addFavorita(cancha: CanchaFavorita) {
        viewModelScope.launch {
            repository.addFavorita(cancha)
        }
    }
    
    /**
     * Elimina una cancha de favoritos
     */
    fun removeFavorita(complexId: String) {
        viewModelScope.launch {
            repository.removeFavorita(complexId)
        }
    }
    
    /**
     * Verifica si una cancha está en favoritos
     * Suspending function - debe usarse en coroutine
     */
    suspend fun isFavorita(complexId: String): Boolean {
        return repository.isFavorita(complexId)
    }
}
