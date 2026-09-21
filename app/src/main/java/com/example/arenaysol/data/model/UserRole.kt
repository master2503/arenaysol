package com.example.arenaysol.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ADMIN,
    WAITER,
    KITCHEN,
    DOORMAN
}
