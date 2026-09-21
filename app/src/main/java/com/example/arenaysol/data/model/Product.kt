package com.example.arenaysol.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val imageUrl: String? = null
)
