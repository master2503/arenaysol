package com.example.arenaysol.data.local

import androidx.room.TypeConverter
import com.example.arenaysol.data.model.OrderStatus

class Converters {
    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String {
        return value.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}
