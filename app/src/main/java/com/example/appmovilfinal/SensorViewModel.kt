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

    // Agrega estas variables para login y registro
    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse

    private val _registerResponse = MutableStateFlow<RegisterResponse?>(null)
    val registerResponse: StateFlow<RegisterResponse?> = _registerResponse

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

    // Función de login
    fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val call = ApiClient.apiService.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    // Guardar el token o cualquier otra información que necesites
                    // Ejemplo: response.body()?.access
                } else {
                    Log.e("SensorViewModel", "Error en la respuesta del login: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("SensorViewModel", "Error en la petición del login: ${t.message}")
            }
        })
    }

    // Función de registro
    fun register(username: String, password: String, email: String) {
        val registerRequest = RegisterRequest(username, password, email)
        val call = ApiClient.apiService.register(registerRequest)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    Log.e("SensorViewModel", "Error en la respuesta del registro: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("SensorViewModel", "Error en la petición del registro: ${t.message}")
            }
        })
    }
}
