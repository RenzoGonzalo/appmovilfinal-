package com.example.appmovilfinal

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorViewModel : ViewModel() {

    private val _datos = MutableStateFlow<List<SensorData>>(emptyList())
    val datos: StateFlow<List<SensorData>> get() = _datos

    private val _valoresExtremos = MutableStateFlow<ExtremosResponse?>(null)
    val valoresExtremos: StateFlow<ExtremosResponse?> = _valoresExtremos

    // Agrega estas variables para login y registro
    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse

    private val _registerResponse = MutableStateFlow<RegisterResponse?>(null)
    val registerResponse: StateFlow<RegisterResponse?> = _registerResponse

    // Estado para la carga y los errores
    private val _cargandoDatos = MutableStateFlow(false)
    val cargandoDatos: StateFlow<Boolean> = _cargandoDatos

    private val _errorDatos = MutableStateFlow<String?>(null)
    val errorDatos: StateFlow<String?> = _errorDatos

    fun obtenerDatos(fecha: String) {
        _cargandoDatos.value = true // Iniciar carga
        val call = ApiClient.apiService.obtenerDatos(fecha)

        call.enqueue(object : Callback<List<SensorData>> {
            override fun onResponse(call: Call<List<SensorData>>, response: Response<List<SensorData>>) {
                _cargandoDatos.value = false // Detener carga
                if (response.isSuccessful) {
                    response.body()?.let { listaDatos ->
                        _datos.value = listaDatos // Actualizar el flujo con los datos obtenidos

                        // Log para verificar los IDs
                        listaDatos.forEach { dato ->
                            Log.d("SensorViewModel", "ID: ${dato.id}, Sensor1Force: ${dato.sensor1Force}")
                        }
                    } ?: run {
                        _errorDatos.value = "Error: La respuesta no contiene datos."
                        Log.e("SensorViewModel", "La respuesta no contiene datos.")
                    }
                } else {
                    _errorDatos.value = "Error en la respuesta: ${response.message()}"
                    Log.e("SensorViewModel", "Error en la respuesta: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SensorData>>, t: Throwable) {
                _cargandoDatos.value = false // Detener carga
                _errorDatos.value = "Error en la petición: ${t.message}"
                Log.e("SensorViewModel", "Error en la petición: ${t.message}")
            }
        })
    }

    fun obtenerValoresExtremos(fecha: String) {
        _cargandoDatos.value = true // Iniciar carga
        val call = ApiClient.apiService.obtenerValoresExtremos(fecha)

        call.enqueue(object : Callback<ExtremosResponse> {
            override fun onResponse(call: Call<ExtremosResponse>, response: Response<ExtremosResponse>) {
                _cargandoDatos.value = false // Detener carga
                if (response.isSuccessful) {
                    // Aquí asegúrate de que _valoresExtremos está siendo actualizado con la respuesta
                    _valoresExtremos.value = response.body()
                } else {
                    _errorDatos.value = "Error en la respuesta: ${response.message()}"
                    Log.e("SensorViewModel", "Error en la respuesta: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ExtremosResponse>, t: Throwable) {
                _cargandoDatos.value = false // Detener carga
                _errorDatos.value = "Error en la petición: ${t.message}"
                Log.e("SensorViewModel", "Error en la petición: ${t.message}")
            }
        })
    }

    // Función para eliminar un dato específico
    fun eliminarDato(id: Int) {
        _cargandoDatos.value = true // Iniciar carga

        val call = ApiClient.apiService.eliminarDato(id)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _cargandoDatos.value = false  // Detener carga

                if (response.isSuccessful) {
                    // El dato fue eliminado correctamente
                    Log.d("SensorViewModel", "Dato eliminado exitosamente")

                    // Actualiza la lista de datos, eliminando el dato que ya no existe
                    _datos.value = _datos.value.filterNot { it.id == id }

                    // Aquí también puedes agregar lógica para mostrar un mensaje o realizar alguna acción
                } else {
                    Log.e("SensorViewModel", "Error al eliminar el dato: ${response.message()}")
                    _errorDatos.value = "Error al eliminar el dato: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _cargandoDatos.value = false  // Detener carga
                Log.e("SensorViewModel", "Error en la petición de eliminación: ${t.message}")
                _errorDatos.value = "Error en la petición de eliminación: ${t.message}"
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
