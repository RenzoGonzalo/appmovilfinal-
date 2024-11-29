package com.example.appmovilfinal

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.43.60:8000/"

    val apiService: ApiService by lazy {
        // Crear una instancia de Moshi y agregar el KotlinJsonAdapterFactory
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())  // Agregar esta línea
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))  // Usar el Moshi con KotlinJsonAdapterFactory
            .build()
            .create(ApiService::class.java)
    }
}
