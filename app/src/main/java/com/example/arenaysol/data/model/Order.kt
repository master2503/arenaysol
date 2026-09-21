package com.example.arenaysol.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val tableNumber: Int,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val timestamp: Long,
    val total: Double
)

@Serializable
data class OrderItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val notes: String? = null
)

@Serializable
enum class OrderStatus {
    PENDING,
    PREPARING,
    READY,
    DELIVERED,
    PAID,
    CANCELLED
}
