package com.example.appmovilfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.appmovilfinal.ui.theme.AppMovilFinalTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: SensorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SensorViewModel::class.java)

        setContent {
            AppMovilFinalTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavHost(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(viewModel: SensorViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel, navController)
        }
        composable("register") {
            RegisterScreen(viewModel, navController)
        }
        composable("obtenerDatos") {  // Ya no necesitamos el parámetro de fecha
            ObtenerDatosScreen(viewModel)  // Llamamos sin la fecha estática
        }
        composable("obtenerValoresExtremos") {
            ValoresExtremosScreen(viewModel)
        }
    }
}


@Composable
fun LoginScreen(viewModel: SensorViewModel, navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password)
                navController.navigate("obtenerDatos") // Navegar a la pantalla de obtenerDatos
            }
        }) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@Composable
fun RegisterScreen(viewModel: SensorViewModel, navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                viewModel.register(username, password, email)
                navController.navigate("login") // Navegar al Login después de registrarse
            }
        }) {
            Text("Registrar")
        }
    }
}

@Composable
fun ObtenerDatosScreen(viewModel: SensorViewModel) {
    var fechaInput by remember { mutableStateOf("") }  // Campo de fecha editable
    val cargandoDatos by viewModel.cargandoDatos.collectAsState(initial = false)
    val datos by viewModel.datos.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Campo de texto para ingresar la fecha
        TextField(
            value = fechaInput,
            onValueChange = { fechaInput = it },
            label = { Text("Ingresa la fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para obtener los datos
        Button(onClick = {
            if (fechaInput.isNotEmpty()) {
                viewModel.obtenerDatos(fechaInput) // Llamar a obtener los datos con la fecha ingresada
            }
        }) {
            Text("Obtener Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para obtener los valores extremos
        Button(onClick = {
            if (fechaInput.isNotEmpty()) {
                // Navegar a la ruta de obtener los valores extremos
                viewModel.obtenerValoresExtremos(fechaInput) // Llamar a obtener los valores extremos
            }
        }) {
            Text("Obtener Valores Extremos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar indicador de carga
        if (cargandoDatos) {
            CircularProgressIndicator()
        }

        // Mostrar los datos cuando estén disponibles
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(datos) { dato ->
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Sensor1: ${dato.sensor1Force}")
                    Text("Sensor2: ${dato.sensor2Force}")
                    Text("Sensor3: ${dato.sensor3Force}")
                    Text("Sensor4: ${dato.sensor4Force}")
                    Text("Sensor5: ${dato.sensor5Force}")
                    Text("Total: ${dato.totalForce}")
                    Text("Hora: ${dato.readableTime}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun ValoresExtremosScreen(viewModel: SensorViewModel) {
    // Obtenemos los valores de la UI State del ViewModel
    val cargandoDatos by viewModel.cargandoDatos.collectAsState(initial = false)
    val valoresExtremos by viewModel.valoresExtremos.collectAsState(initial = null)
    val errorDatos by viewModel.errorDatos.collectAsState(initial = "")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Campo de texto para ingresar la fecha
        var fechaInput by remember { mutableStateOf("") }

        // Campo de texto para ingresar la fecha
        TextField(
            value = fechaInput,
            onValueChange = { fechaInput = it },
            label = { Text("Ingresa la fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para obtener los valores extremos
        Button(onClick = {
            if (fechaInput.isNotEmpty()) {
                viewModel.obtenerValoresExtremos(fechaInput) // Llamar a obtener los valores extremos con la fecha ingresada
            }
        }) {
            Text("Obtener Valores Extremos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar indicador de carga mientras se obtienen los datos
        if (cargandoDatos) {
            CircularProgressIndicator()
        }

        // Mostrar los valores extremos cuando estén disponibles
        if (valoresExtremos != null) {
            Text("Fecha: ${valoresExtremos?.fecha}")
            Text("Valor Máximo: ${valoresExtremos?.max_total ?: "No disponible"}")
            Text("Valor Mínimo: ${valoresExtremos?.min_total ?: "No disponible"}")
        }

        // Mostrar error si ocurre un problema

    }
}
