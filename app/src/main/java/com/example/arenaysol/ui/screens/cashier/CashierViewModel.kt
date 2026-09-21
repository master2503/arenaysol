package com.example.arenaysol.ui.screens.cashier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arenaysol.data.model.Order
import com.example.arenaysol.data.model.OrderStatus
import com.example.arenaysol.data.network.client.KtorClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashierViewModel @Inject constructor(
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    fun connectToServer(host: String) {
        viewModelScope.launch {
            try {
                ktorClient.init(host)
                refreshOrders()
                _isConnected.value = true
                observeUpdates(host)
            } catch (e: Exception) {
                _isConnected.value = false
            }
        }
    }

    private suspend fun refreshOrders() {
        _orders.value = ktorClient.getOrders()
    }

    private fun observeUpdates(host: String) {
        viewModelScope.launch {
            ktorClient.observeUpdates(host).collect {
                refreshOrders()
            }
        }
    }

    fun markAsPaid(orderId: String) {
        viewModelScope.launch {
            ktorClient.updateOrderStatus(orderId, OrderStatus.PAID.name)
            refreshOrders()
        }
    }
}
