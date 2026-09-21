package com.example.arenaysol.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startServer()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel de Administración") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            item {
                Text("Inventario", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(products) { product ->
                ListItem(
                    headlineContent = { Text(product.name) },
                    supportingContent = { Text("${product.category} - Stock: ${product.stock}") },
                    trailingContent = { Text("$${product.price}") }
                )
                HorizontalDivider()
            }
        }

        if (showAddDialog) {
            AddProductDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, price, category, stock ->
                    viewModel.addProduct(name, price, category, stock)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(name, price.toDoubleOrNull() ?: 0.0, category, stock.toIntOrNull() ?: 0)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
