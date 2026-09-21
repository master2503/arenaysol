package com.example.arenaysol.data.local.dao

import androidx.room.*
import com.example.arenaysol.data.local.entity.WristbandSaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WristbandSaleDao {
    @Query("SELECT * FROM wristband_sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<WristbandSaleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: WristbandSaleEntity)
}
