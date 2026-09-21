package com.example.arenaysol.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.arenaysol.data.local.dao.OrderDao
import com.example.arenaysol.data.local.dao.ProductDao
import com.example.arenaysol.data.local.dao.WristbandSaleDao
import com.example.arenaysol.data.local.entity.OrderEntity
import com.example.arenaysol.data.local.entity.OrderItemEntity
import com.example.arenaysol.data.local.entity.ProductEntity
import com.example.arenaysol.data.local.entity.WristbandSaleEntity

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        WristbandSaleEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun wristbandSaleDao(): WristbandSaleDao
}
