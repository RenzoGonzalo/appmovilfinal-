package com.example.appmovilfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier // Importar Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.appmovilfinal.ui.theme.AppMovilFinalTheme // Verifica que este nombre sea el correcto

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: SensorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SensorViewModel::class.java)

        setContent {
            AppMovilFinalTheme { // Verifica que el nombre de tu tema esté correcto
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        var fecha by remember { mutableStateOf("") }

                        // Campo de texto para la fecha
                        TextField(
                            value = fecha,
                            onValueChange = { fecha = it },
                            label = { Text("Fecha") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para obtener los datos
                        Button(onClick = {
                            if (fecha.isNotEmpty()) {
                                viewModel.obtenerDatos(fecha)
                            }
                        }) {
                            Text("Obtener Datos")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar los datos obtenidos
                        val datos = viewModel.datos.collectAsState()
                        datos.value?.let {
                            it.forEach { data ->
                                Text("Sensor 1: ${data.sensor1Force}")
                                Text("Sensor 2: ${data.sensor2Force}")
                                Text("Sensor 3: ${data.sensor3Force}")
                                Text("Sensor 4: ${data.sensor4Force}")
                                Text("Sensor 5: ${data.sensor5Force}")
                                Text("Total Force: ${data.totalForce}")
                                Text("Hora: ${data.readableTime}")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
