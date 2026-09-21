package com.example.arenaysol.ui.screens.waiter

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arenaysol.data.model.Order
import com.example.arenaysol.data.model.OrderItem
import com.example.arenaysol.data.model.OrderStatus
import com.example.arenaysol.data.model.Product
import com.example.arenaysol.data.network.client.KtorClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WaiterViewModel @Inject constructor(
    private val ktorClient: KtorClient
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    val cart = mutableStateListOf<OrderItem>()

    fun connectToServer(host: String) {
        viewModelScope.launch {
            try {
                ktorClient.init(host)
                refreshProducts()
                _isConnected.value = true
            } catch (e: Exception) {
                _isConnected.value = false
            }
        }
    }

    private suspend fun refreshProducts() {
        _products.value = ktorClient.getProducts()
    }

    fun addToCart(product: Product) {
        val existing = cart.find { it.productId == product.id }
        if (existing != null) {
            val index = cart.indexOf(existing)
            cart[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cart.add(OrderItem(product.id, product.name, 1, product.price))
        }
    }

    fun sendOrder(tableNumber: Int) {
        viewModelScope.launch {
            val order = Order(
                id = UUID.randomUUID().toString(),
                tableNumber = tableNumber,
                items = cart.toList(),
                status = OrderStatus.PENDING,
                timestamp = System.currentTimeMillis(),
                total = cart.sumOf { it.price * it.quantity }
            )
            ktorClient.postOrder(order)
            cart.clear()
        }
    }
}
