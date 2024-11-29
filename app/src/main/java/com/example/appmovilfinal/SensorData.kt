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
