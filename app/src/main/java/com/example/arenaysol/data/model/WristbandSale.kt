package com.example.arenaysol.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WristbandSale(
    val id: String,
    val type: String, // e.g., "General", "VIP"
    val price: Double,
    val quantity: Int,
    val timestamp: Long,
    val sellerId: String
)
