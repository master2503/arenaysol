package com.example.arenaysol.ui.screens.role

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arenaysol.data.model.UserRole
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Selecciona tu Rol", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        UserRole.entries.forEach { role ->
            Button(
                onClick = { 
                    if (role == UserRole.ADMIN) {
                        showPinDialog = true
                    } else {
                        onRoleSelected(role)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(role.name)
            }
        }
    }

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Seguridad Admin") },
            text = {
                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Introduce PIN (1234)") },
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (pin == "1234") {
                        onRoleSelected(UserRole.ADMIN)
                        showPinDialog = false
                    }
                }) {
                    Text("Ingresar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
