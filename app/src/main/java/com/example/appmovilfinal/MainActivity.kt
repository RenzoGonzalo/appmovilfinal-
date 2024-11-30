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
        composable("obtenerDatos") {
            MostrarDatos(viewModel)
        }
        composable("obtenerValoresExtremos") {
            MostrarValoresExtremos(viewModel)
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
fun MostrarDatos(viewModel: SensorViewModel) {
    val datos by viewModel.datos.collectAsState(initial = null)

    if (datos == null) {
        // Indicador de carga mientras se obtienen los datos
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        // Aseguramos que datos es una lista antes de usarlo
        val listaDatos = datos ?: emptyList() // Evitar null
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(listaDatos) { dato ->
                Text("Sensor1: ${dato.sensor1Force}")
                Text("Sensor2: ${dato.sensor2Force}")
                Text("Sensor3: ${dato.sensor3Force}")
                Text("Sensor4: ${dato.sensor4Force}")
                Text("Sensor5: ${dato.sensor5Force}")
                Text("Total: ${dato.totalForce}")
                Text("Hora: ${dato.readableTime}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}


@Composable
fun MostrarValoresExtremos(viewModel: SensorViewModel) {
    val valoresExtremos by viewModel.valoresExtremos.collectAsState(initial = null)

    if (valoresExtremos == null) {
        // Indicador de carga mientras se obtienen los valores extremos
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        valoresExtremos?.let { extremos ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fecha: ${extremos.fecha}")
                Text("Máximo total: ${extremos.max_total ?: "No disponible"}")
                Text("Mínimo total: ${extremos.min_total ?: "No disponible"}")
            }
        }
    }
}
