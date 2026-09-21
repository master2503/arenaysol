package com.example.arenaysol.ui.screens.waiter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaiterScreen(
    viewModel: WaiterViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val products by viewModel.products.collectAsState()
    var hostIp by remember { mutableStateOf("") }
    var tableNumber by remember { mutableStateOf("") }

    if (!isConnected) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Conectar al Servidor", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = hostIp,
                onValueChange = { hostIp = it },
                label = { Text("IP del Servidor (Host)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.connectToServer(hostIp) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Conectar")
            }
        }
    } else {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Mesero - Pedidos") }) }
        ) { padding ->
            Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                // Product List
                LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
                    items(products) { product ->
                        Card(
                            onClick = { viewModel.addToCart(product) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            ListItem(
                                headlineContent = { Text(product.name) },
                                trailingContent = { Text("$${product.price}") }
                            )
                        }
                    }
                }

                // Cart
                Column(modifier = Modifier.weight(0.8f).padding(8.dp)) {
                    Text("Mesa:")
                    OutlinedTextField(value = tableNumber, onValueChange = { tableNumber = it })
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Carrito (${viewModel.cart.size})")
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(viewModel.cart) { item ->
                            Text("${item.productName} x${item.quantity}")
                        }
                    }
                    Button(
                        onClick = { viewModel.sendOrder(tableNumber.toIntOrNull() ?: 0) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar Pedido")
                    }
                }
            }
        }
    }
}
