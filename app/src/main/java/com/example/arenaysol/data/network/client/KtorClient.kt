package com.example.arenaysol.data.network.client

import com.example.arenaysol.data.model.Order
import com.example.arenaysol.data.model.Product
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorClient @Inject constructor() {
    private var client: HttpClient? = null
    private var baseUrl: String = ""

    fun init(host: String, port: Int = 8080) {
        baseUrl = "http://$host:$port"
        client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            install(WebSockets)
        }
    }

    suspend fun getProducts(): List<Product> {
        return client?.get("$baseUrl/products")?.body() ?: emptyList()
    }

    suspend fun getOrders(): List<Order> {
        return client?.get("$baseUrl/orders")?.body() ?: emptyList()
    }

    suspend fun postOrder(order: Order) {
        client?.post("$baseUrl/orders") {
            setBody(order)
            header("Content-Type", "application/json")
        }
    }

    suspend fun postWristbandSale(sale: com.example.arenaysol.data.model.WristbandSale) {
        client?.post("$baseUrl/wristbands") {
            setBody(sale)
            header("Content-Type", "application/json")
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        client?.post("$baseUrl/orders/status") {
            setBody(mapOf("orderId" to orderId, "status" to status))
            header("Content-Type", "application/json")
        }
    }

    fun observeUpdates(host: String, port: Int = 8080): Flow<String> = flow {
        client?.webSocket(host = host, port = port, path = "/updates") {
            for (frame in incoming) {
                val message = receiveDeserialized<String>()
                emit(message)
            }
        }
    }
}
