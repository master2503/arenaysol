package com.example.arenaysol.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arenaysol.data.local.dao.ProductDao
import com.example.arenaysol.data.local.entity.ProductEntity
import com.example.arenaysol.data.network.server.KtorServer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val ktorServer: KtorServer
) : ViewModel() {

    val products = productDao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun startServer() {
        ktorServer.start()
    }

    fun addProduct(name: String, price: Double, category: String, stock: Int) {
        viewModelScope.launch {
            productDao.insertProduct(
                ProductEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    price = price,
                    category = category,
                    stock = stock,
                    imageUrl = null
                )
            )
        }
    }
}
