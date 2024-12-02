package com.example.appmovilfinal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            PanelDeControlScreen(navController) // Nueva pantalla principal
        }
        composable("obtenerDatos") {
            ObtenerDatosScreen(viewModel)
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
                navController.navigate("panelDeControl") // Navegar al Panel de Control
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
    var fechaInput by remember { mutableStateOf("") } // Campo de fecha editable
    val cargandoDatos by viewModel.cargandoDatos.collectAsState(initial = false)
    val datos by viewModel.datos.collectAsState(initial = emptyList())
    val errorDatos by viewModel.errorDatos.collectAsState(initial = null) // Nuevo estado para errores

    // Limpiar estado solo al cambiar la fecha y antes de realizar la nueva consulta
    LaunchedEffect(fechaInput) {
        if (fechaInput.isNotEmpty()) {
            viewModel.limpiarEstado()  // Limpiar estado previo al cambiar la fecha
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = fechaInput,
            onValueChange = { fechaInput = it },
            label = { Text("Ingresa la fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (fechaInput.isNotEmpty()) {
                    viewModel.obtenerDatos(fechaInput)  // Llamar a obtener los datos con la fecha ingresada
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargandoDatos) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        errorDatos?.let { mensajeError ->
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(datos) { dato ->
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("ID: ${dato.id}", style = MaterialTheme.typography.bodyMedium)
                    Text("Sensor1: ${dato.sensor1Force}")
                    Text("Sensor2: ${dato.sensor2Force}")
                    Text("Sensor3: ${dato.sensor3Force}")
                    Text("Sensor4: ${dato.sensor4Force}")
                    Text("Sensor5: ${dato.sensor5Force}")
                    Text("Total: ${dato.totalForce}")
                    Text("Hora: ${dato.readableTime}")

                    Button(
                        onClick = {
                            viewModel.eliminarDato(dato.id)  // Llamar a la función eliminarDato
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Eliminar Dato")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
            }
        }
    }
}

@Composable
fun ValoresExtremosScreen(viewModel: SensorViewModel) {
    // Obtenemos los valores de la UI State del ViewModel
    val cargandoDatos by viewModel.cargandoDatos.collectAsState(initial = false)
    val valoresExtremos by viewModel.valoresExtremos.collectAsState(initial = null)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Fila para mostrar la entrada de fecha y el botón
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            var fechaInput by remember { mutableStateOf("") }

            // Campo de texto para ingresar la fecha
            TextField(
                value = fechaInput,
                onValueChange = { fechaInput = it },
                label = { Text("Ingresa la fecha (YYYY-MM-DD)") },
                modifier = Modifier.weight(1f) // Esto hace que ocupe el espacio disponible
            )

            // Botón para obtener los valores extremos
            Button(onClick = {
                if (fechaInput.isNotEmpty()) {
                    viewModel.obtenerValoresExtremos(fechaInput) // Llamar a obtener los valores extremos
                }
            }) {
                Text("Obtener Valores Extremos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar indicador de carga mientras se obtienen los datos
        if (cargandoDatos) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los resultados en una columna en la parte inferior
        valoresExtremos?.let { extremos ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Fecha: ${extremos.fecha}")
                Text("Valor Máximo: ${extremos.max_total?.toString() ?: "No disponible"}")
                Text("Valor Mínimo: ${extremos.min_total?.toString() ?: "No disponible"}")
            }
        }
    }
}
@Composable
fun PanelDeControlScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Panel de Control",
            fontSize = 24.sp, // Tamaño de la fuente
            fontWeight = FontWeight.Bold // Estilo de la fuente
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a ObtenerDatosScreen
        Button(onClick = { navController.navigate("obtenerDatos") }) {
            Text("Ir a Obtener Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a ValoresExtremosScreen
        Button(onClick = { navController.navigate("obtenerValoresExtremos") }) {
            Text("Ir a Valores Extremos")
        }
    }
}