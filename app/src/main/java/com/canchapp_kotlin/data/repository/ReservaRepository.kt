package com.canchapp_kotlin.data.repository

import com.canchapp_kotlin.data.local.Reserva
import com.canchapp_kotlin.data.local.ReservaDao
import kotlinx.coroutines.flow.Flow

class ReservaRepository(private val dao: ReservaDao) {

    fun getAllFlow(): Flow<List<Reserva>> = dao.getAllFlow()

    suspend fun add(reserva: Reserva) = dao.insert(reserva)

    suspend fun remove(id: Int) = dao.deleteById(id)

    suspend fun hasReserva(complexId: String): Boolean =
        dao.countByComplexId(complexId) > 0
}
