package com.example.appmovilfinal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/obtener_datos/")
    fun obtenerDatos(@Query("fecha") fecha: String): Call<List<SensorData>>

    @GET("api/recibir/")
    fun recibirDatos(@Query("sensorData") sensorData: String): Call<Void>
}
