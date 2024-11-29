package com.example.appmovilfinal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorViewModel : ViewModel() {
    private val _datos = MutableStateFlow<List<SensorData>?>(null)
    val datos: StateFlow<List<SensorData>?> = _datos

    private val _valoresExtremos = MutableStateFlow<ExtremosResponse?>(null)
    val valoresExtremos: StateFlow<ExtremosResponse?> = _valoresExtremos

    fun obtenerDatos(fecha: String) {
        val call = ApiClient.apiService.obtenerDatos(fecha)

        call.enqueue(object : Callback<List<SensorData>> {
            override fun onResponse(call: Call<List<SensorData>>, response: Response<List<SensorData>>) {
                if (response.isSuccessful) {
                    _datos.value = response.body()
                } else {
                    Log.e("SensorViewModel", "Error en la respuesta: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SensorData>>, t: Throwable) {
                Log.e("SensorViewModel", "Error en la petición: ${t.message}")
            }
        })
    }

    fun obtenerValoresExtremos(fecha: String) {
        val call = ApiClient.apiService.obtenerValoresExtremos(fecha)

        call.enqueue(object : Callback<ExtremosResponse> {
            override fun onResponse(call: Call<ExtremosResponse>, response: Response<ExtremosResponse>) {
                if (response.isSuccessful) {
                    _valoresExtremos.value = response.body()
                } else {
                    Log.e("SensorViewModel", "Error en la respuesta: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ExtremosResponse>, t: Throwable) {
                Log.e("SensorViewModel", "Error en la petición: ${t.message}")
            }
        })
    }
}
