package com.example.appmovilfinal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/obtener_datos/")  // Asegúrate de que coincida con la nueva ruta
    fun obtenerDatos(@Query("fecha") fecha: String): Call<List<SensorData>>

    @GET("api/recibir/")  // Asegúrate de que coincida con la nueva ruta
    fun recibirDatos(@Query("sensorData") sensorData: String): Call<Void>

    @GET("api/valores-extremos/")  // Asegúrate de que coincida con la nueva ruta
    fun obtenerValoresExtremos(@Query("fecha") fecha: String): Call<ExtremosResponse>
}
