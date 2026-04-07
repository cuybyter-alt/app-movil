package com.canchapp_kotlin.data.repository

import com.canchapp_kotlin.data.network.ApiService
import com.canchapp_kotlin.data.network.LoginRequest
import com.canchapp_kotlin.data.network.LoginResponse
import com.canchapp_kotlin.data.network.RegisterRequest
import com.canchapp_kotlin.data.network.RegisterResponse
import com.canchapp_kotlin.data.network.RetrofitClient
import com.canchapp_kotlin.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class AuthRepository(
    private val api: ApiService = RetrofitClient.api
) {

    suspend fun login(identifier: String, password: String): Resource<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(identifier, password))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val rawBody = response.errorBody()?.string()
                val errorResponse = parseError<LoginResponse>(rawBody)
                val message = buildErrorMessage(
                    mainMessage = errorResponse?.error?.message,
                    details     = extractDetails(rawBody),
                    fallback    = "Error ${response.code()}: intenta de nuevo"
                )
                Resource.Error(message)
            }
        } catch (e: IOException) {
            Resource.Error("Sin conexión a internet")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun register(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        roleName: String,
        username: String
    ): Resource<RegisterResponse> {
        return try {
            val response = api.register(
                RegisterRequest(email, firstName, lastName, password, roleName, username)
            )
            when (response.code()) {
                201 -> Resource.Success(response.body()!!)
                else -> {
                    val rawBody = response.errorBody()?.string()
                    val errorResponse = parseError<RegisterResponse>(rawBody)
                    val message = buildErrorMessage(
                        mainMessage = errorResponse?.error?.message,
                        details     = extractDetails(rawBody),
                        fallback    = when (response.code()) {
                            409  -> "El usuario o correo ya está registrado"
                            500  -> "Error interno del servidor, intenta más tarde"
                            else -> "Error ${response.code()}: intenta de nuevo"
                        }
                    )
                    Resource.Error(message)
                }
            }
        } catch (e: IOException) {
            Resource.Error("Sin conexión a internet")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    // Extrae el mapa de details del JSON crudo para mostrar errores por campo
    private fun extractDetails(rawBody: String?): Map<String, List<String>>? {
        if (rawBody == null) return null
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val root = Gson().fromJson<Map<String, Any>>(rawBody, type)
            val error = root["error"] as? Map<*, *> ?: return null
            val details = error["details"] as? Map<*, *> ?: return null
            details.mapNotNull { (k, v) ->
                val key = k.toString()
                val values = when (v) {
                    is List<*> -> v.map { it.toString() }
                    is String  -> listOf(v)
                    else       -> listOf(v.toString())
                }
                key to values
            }.toMap()
        } catch (e: Exception) { null }
    }

    private fun buildErrorMessage(
        mainMessage: String?,
        details: Map<String, List<String>>?,
        fallback: String
    ): String {
        val base = mainMessage ?: fallback
        if (details.isNullOrEmpty()) return base
        val fieldErrors = details.entries.joinToString("\n") { (field, msgs) ->
            val label = fieldLabels[field] ?: field
            "• $label: ${msgs.firstOrNull() ?: ""}"
        }
        return "$base\n$fieldErrors"
    }

    private val fieldLabels = mapOf(
        "identifier" to "Usuario/correo",
        "password"   to "Contraseña",
        "email"      to "Correo",
        "username"   to "Usuario",
        "f_name"     to "Nombre",
        "l_name"     to "Apellido",
        "role_name"  to "Tipo de cuenta"
    )

    private inline fun <reified T> parseError(errorBody: String?): T? =
        runCatching { Gson().fromJson(errorBody, T::class.java) }.getOrNull()
}

