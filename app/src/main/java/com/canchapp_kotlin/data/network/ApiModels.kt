package com.canchapp_kotlin.data.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val data: LoginData?,
    val error: ApiError?
)

data class LoginData(
    val access: String,
    val refresh: String,
    val user: UserDto
)

data class RegisterRequest(
    val email: String,
    @SerializedName("f_name") val firstName: String,
    @SerializedName("l_name") val lastName: String,
    val password: String,
    @SerializedName("role_name") val roleName: String,
    val username: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String?,
    val data: UserDto?,
    val error: ApiError?
)

data class UserDto(
    @SerializedName("user_id")    val userId: String,
    val username: String,
    val email: String,
    @SerializedName("f_name")     val firstName: String,
    @SerializedName("l_name")     val lastName: String,
    @SerializedName("role_name")  val roleName: String,
    val status: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("is_guest")   val isGuest: Boolean
)

data class ApiError(
    val code: String?,
    val message: String?,
    val details: Map<String, Any>?
)

data class ComplexDto(
    @SerializedName("complex_id")   val complexId: String,
    @SerializedName("owner_id")     val ownerId: String,
    val name: String,
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    @SerializedName("fields_count") val fieldsCount: Int,
    @SerializedName("min_price")    val minPrice: Double,
    @SerializedName("max_price")    val maxPrice: Double,
    @SerializedName("distance_km")  val distanceKm: Double?
)

data class ComplexListData(
    val items: List<ComplexDto>,
    val total: Int,
    val page: Int,
    @SerializedName("page_size")    val pageSize: Int,
    @SerializedName("total_pages")  val totalPages: Int
)

data class ComplexListResponse(
    val success: Boolean,
    val message: String?,
    val data: ComplexListData?,
    val error: ApiError?
)
