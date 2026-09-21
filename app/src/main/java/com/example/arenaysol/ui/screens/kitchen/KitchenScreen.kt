package com.example.arenaysol.ui.screens.kitchen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenScreen(
    viewModel: KitchenViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val orders by viewModel.orders.collectAsState()
    var hostIp by remember { mutableStateOf("") }

    if (!isConnected) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Conectar al Servidor (Cocina)", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = hostIp,
                onValueChange = { hostIp = it },
                label = { Text("IP del Host") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { viewModel.connectToServer(hostIp) }) {
                Text("Conectar")
            }
        }
    } else {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Cocina - Pedidos Entrantes") }) }
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 250.dp),
                modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp)
            ) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Mesa ${order.tableNumber}", style = MaterialTheme.typography.titleLarge)
                            Text("Estado: ${order.status.name}")
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            order.items.forEach { item ->
                                Text("${item.productName} x${item.quantity}")
                            }
                        }
                    }
                }
            }
        }
    }
}
