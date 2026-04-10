package com.canchapp_kotlin.ui.reservas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.canchapp_kotlin.data.local.CanchAppDatabase
import com.canchapp_kotlin.data.local.Reserva
import com.canchapp_kotlin.data.repository.ReservaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ReservaRepository(
        CanchAppDatabase.getDatabase(application).reservaDao()
    )

    val reservas: StateFlow<List<Reserva>> = repo.getAllFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(reserva: Reserva) {
        viewModelScope.launch { repo.add(reserva) }
    }

    fun remove(id: Int) {
        viewModelScope.launch { repo.remove(id) }
    }

    suspend fun hasReserva(complexId: String): Boolean = repo.hasReserva(complexId)
}
