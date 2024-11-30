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
import androidx.compose.ui.Alignment


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
        composable("panelDeControl") {
            PanelDeControl(viewModel, navController)
        }
        composable("obtenerDatos/{fecha}") { backStackEntry ->
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            MostrarDatos(viewModel, fecha)
        }
        composable("obtenerValoresExtremos/{fecha}") { backStackEntry ->
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            MostrarValoresExtremos(viewModel, fecha)
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
fun PanelDeControl(viewModel: SensorViewModel, navController: NavHostController) {
    var fecha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                navController.navigate("obtenerDatos/$fecha") // Navegar a la pantalla de datos
            }
        }) {
            Text("Obtener Datos")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para obtener los valores extremos
        Button(onClick = {
            if (fecha.isNotEmpty()) {
                navController.navigate("obtenerValoresExtremos/$fecha") // Navegar a la pantalla de valores extremos
            }
        }) {
            Text("Obtener Valores Extremos")
        }
    }
}

@Composable
fun MostrarDatos(viewModel: SensorViewModel, fecha: String) {
    // Llamar a obtenerDatos con la fecha
    LaunchedEffect(fecha) {
        viewModel.obtenerDatos(fecha)
    }

    val datos = viewModel.datos.collectAsState().value ?: emptyList()
    val cargando = viewModel.cargandoDatos.collectAsState().value
    val error = viewModel.errorDatos.collectAsState().value

    // Mostrar indicador de carga mientras se obtienen los datos
    if (cargando) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        // Mostrar error si existe
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Error: $error")
        }
    } else {
        // Mostrar datos si están disponibles
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(datos) { dato ->
                Text("Sensor 1: ${dato.sensor1Force}")
                Text("Sensor 2: ${dato.sensor2Force}")
                Text("Sensor 3: ${dato.sensor3Force}")
                Text("Sensor 4: ${dato.sensor4Force}")
                Text("Sensor 5: ${dato.sensor5Force}")
                Text("Total Force: ${dato.totalForce}")
                Text("Hora: ${dato.readableTime}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun MostrarValoresExtremos(viewModel: SensorViewModel, fecha: String) {
    // Llamar a obtenerValoresExtremos con la fecha
    LaunchedEffect(fecha) {
        viewModel.obtenerValoresExtremos(fecha)
    }

    val valoresExtremos by viewModel.valoresExtremos.collectAsState(initial = null)

    if (valoresExtremos == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        valoresExtremos?.let { extremos ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Máximo Total: ${extremos.max_total ?: "No disponible"}")
                Text("Mínimo Total: ${extremos.min_total ?: "No disponible"}")
            }
        }
    }
}