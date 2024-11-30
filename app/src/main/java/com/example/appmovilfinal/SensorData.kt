package com.example.appmovilfinal

import com.squareup.moshi.Json

data class SensorData(
    @Json(name = "sensor1Force") val sensor1Force: Float,
    @Json(name = "sensor2Force") val sensor2Force: Float,
    @Json(name = "sensor3Force") val sensor3Force: Float,
    @Json(name = "sensor4Force") val sensor4Force: Float,
    @Json(name = "sensor5Force") val sensor5Force: Float,
    @Json(name = "totalForce") val totalForce: Float,
    @Json(name = "readableTime") val readableTime: String
)
data class ExtremosResponse(
    val fecha: String,
    val max_total: Float?,
    val min_total: Float?
)

data class LoginRequest(
    val username: String,
    val password: String
)
data class LoginResponse(
    val access: String,
    val refresh: String
)
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)
data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String
)

