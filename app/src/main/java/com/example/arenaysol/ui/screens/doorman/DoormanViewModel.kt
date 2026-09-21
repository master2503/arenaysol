package com.example.arenaysol.ui.screens.doorman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arenaysol.data.model.WristbandSale
import com.example.arenaysol.data.network.client.KtorClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DoormanViewModel @Inject constructor(
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    fun connectToServer(host: String) {
        viewModelScope.launch {
            try {
                ktorClient.init(host)
                _isConnected.value = true
            } catch (e: Exception) {
                _isConnected.value = false
            }
        }
    }

    fun sellWristband(type: String, price: Double, quantity: Int) {
        viewModelScope.launch {
            val sale = WristbandSale(
                id = UUID.randomUUID().toString(),
                type = type,
                price = price,
                quantity = quantity,
                timestamp = System.currentTimeMillis(),
                sellerId = "doorman_1" // Simplificado
            )
            ktorClient.postWristbandSale(sale)
        }
    }
}
