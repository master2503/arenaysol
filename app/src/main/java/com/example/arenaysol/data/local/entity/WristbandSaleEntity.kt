package com.example.arenaysol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wristband_sales")
data class WristbandSaleEntity(
    @PrimaryKey val id: String,
    val type: String,
    val price: Double,
    val quantity: Int,
    val timestamp: Long,
    val sellerId: String
)
