package com.canchapp_kotlin.data.repository

import com.canchapp_kotlin.data.network.ComplexListResponse
import com.canchapp_kotlin.data.network.RetrofitClient
import com.canchapp_kotlin.util.Resource
import java.io.IOException

class ComplexRepository {

    private val api = RetrofitClient.api

    suspend fun getComplexes(
        page: Int = 1,
        lat: Double? = null,
        lon: Double? = null
    ): Resource<ComplexListResponse> {
        return try {
            val response = api.getComplexes(page, lat, lon)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Resource.Success(body)
                } else {
                    Resource.Error(body?.message ?: "Error al obtener complejos")
                }
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Sin conexión a internet")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }
}
