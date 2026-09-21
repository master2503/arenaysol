package com.example.arenaysol.ui.screens.cashier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arenaysol.data.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashierScreen(
    viewModel: CashierViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val orders by viewModel.orders.collectAsState()
    var hostIp by remember { mutableStateOf("") }

    if (!isConnected) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Conectar al Servidor (Caja)", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(value = hostIp, onValueChange = { hostIp = it }, label = { Text("IP Host") })
            Button(onClick = { viewModel.connectToServer(hostIp) }) { Text("Conectar") }
        }
    } else {
        Scaffold(topBar = { TopAppBar(title = { Text("Cajero - Gestión de Pagos") }) }) { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                items(orders.filter { it.status != OrderStatus.PAID }) { order ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Mesa ${order.tableNumber}", style = MaterialTheme.typography.titleLarge)
                            Text("Total: $${order.total}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.markAsPaid(order.id) }) {
                                Text("Marcar como PAGADO")
                            }
                        }
                    }
                }
            }
        }
    }
}
