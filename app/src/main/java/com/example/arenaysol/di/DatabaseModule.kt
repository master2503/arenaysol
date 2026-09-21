package com.example.arenaysol.di

import android.content.Context
import androidx.room.Room
import com.example.arenaysol.data.local.AppDatabase
import com.example.arenaysol.data.local.dao.OrderDao
import com.example.arenaysol.data.local.dao.ProductDao
import com.example.arenaysol.data.local.dao.WristbandSaleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "arenaysol_db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    fun provideWristbandSaleDao(database: AppDatabase): WristbandSaleDao {
        return database.wristbandSaleDao()
    }
}
