package com.example.arenaysol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arenaysol.data.model.OrderStatus

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val tableNumber: Int,
    val status: OrderStatus,
    val timestamp: Long,
    val total: Double
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val notes: String?
)
