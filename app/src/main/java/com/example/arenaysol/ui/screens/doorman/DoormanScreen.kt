package com.example.arenaysol.ui.screens.doorman

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoormanScreen(
    viewModel: DoormanViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()
    var hostIp by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }

    if (!isConnected) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Conectar al Servidor (Portería)")
            OutlinedTextField(value = hostIp, onValueChange = { hostIp = it }, label = { Text("IP Host") })
            Button(onClick = { viewModel.connectToServer(hostIp) }) { Text("Conectar") }
        }
    } else {
        Scaffold(topBar = { TopAppBar(title = { Text("Portería - Venta de Manillas") }) }) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text("Tipo de Manilla: General")
                Text("Precio: $10.0") // Ejemplo
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Cantidad") })
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.sellWristband("General", 10.0, quantity.toIntOrNull() ?: 1) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vender Manilla")
                }
            }
        }
    }
}
