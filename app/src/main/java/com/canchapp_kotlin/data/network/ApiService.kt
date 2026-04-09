package com.canchapp_kotlin.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/identity/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/identity/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    // Courutines
    @GET("api/complexes/")
    suspend fun getComplexes(
        @Query("page") page: Int = 1,
        @Query("lat")  lat: Double? = null,
        @Query("lon")  lon: Double? = null
    ): Response<ComplexListResponse>
}
