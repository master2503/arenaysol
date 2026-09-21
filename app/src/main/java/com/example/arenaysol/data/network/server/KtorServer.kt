package com.example.arenaysol.data.network.server

import com.example.arenaysol.data.local.dao.OrderDao
import com.example.arenaysol.data.local.dao.ProductDao
import com.example.arenaysol.data.local.entity.OrderEntity
import com.example.arenaysol.data.local.entity.OrderItemEntity
import com.example.arenaysol.data.local.entity.ProductEntity
import com.example.arenaysol.data.model.Order
import com.example.arenaysol.data.model.OrderItem
import com.example.arenaysol.data.model.Product
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorServer @Inject constructor(
    private val productDao: ProductDao,
    private val orderDao: OrderDao,
    private val wristbandSaleDao: com.example.arenaysol.data.local.dao.WristbandSaleDao
) {
    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val wsSessions = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketServerSession>())

    fun start(port: Int = 8080) {
        if (server != null) return

        server = embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                json()
            }
            install(WebSockets)

            routing {
                // Products
                get("/products") {
                    val products = productDao.getAllProducts().first().map { it.toModel() }
                    call.respond(products)
                }

                post("/products") {
                    val product = call.receive<Product>()
                    productDao.insertProduct(product.toEntity())
                    broadcastUpdate("products_updated")
                    call.respond(mapOf("status" to "ok"))
                }

                // Orders
                get("/orders") {
                    val orders = orderDao.getAllOrders().first().map { it.toModel() }
                    call.respond(orders)
                }

                post("/orders") {
                    val order = call.receive<Order>()
                    saveOrder(order)
                    broadcastUpdate("orders_updated")
                    call.respond(mapOf("status" to "ok"))
                }

                // Wristbands
                post("/wristbands") {
                    val sale = call.receive<com.example.arenaysol.data.model.WristbandSale>()
                    wristbandSaleDao.insertSale(sale.toEntity())
                    broadcastUpdate("wristbands_updated")
                    call.respond(mapOf("status" to "ok"))
                }

                // WebSocket for real-time updates
                webSocket("/updates") {
                    wsSessions += this
                    try {
                        for (frame in incoming) {
                            // Keep alive or handle client messages
                        }
                    } catch (e: ClosedReceiveChannelException) {
                        // Session closed
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    } finally {
                        wsSessions -= this
                    }
                }
                post("/orders/status") {
                    val update = call.receive<Map<String, String>>()
                    val orderId = update["orderId"]
                    val status = update["status"]
                    if (orderId != null && status != null) {
                        orderDao.updateOrderStatus(orderId, status)
                        broadcastUpdate("orders_updated")
                        call.respond(mapOf("status" to "ok"))
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }.start(wait = false)
    }

    fun stop() {
        server?.stop(1000, 2000)
        server = null
    }

    private fun broadcastUpdate(message: String) {
        scope.launch {
            wsSessions.forEach { session ->
                try {
                    session.sendSerialized(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun saveOrder(order: Order) {
        orderDao.insertOrder(order.toEntity())
        orderDao.insertOrderItems(order.items.map { it.toEntity(order.id) })
    }
}

// Mappers
fun ProductEntity.toModel() = Product(id, name, price, category, stock, imageUrl)
fun Product.toEntity() = ProductEntity(id, name, price, category, stock, imageUrl)

fun com.example.arenaysol.data.local.dao.OrderWithItems.toModel() = Order(
    id = order.id,
    tableNumber = order.tableNumber,
    items = items.map { it.toModel() },
    status = order.status,
    timestamp = order.timestamp,
    total = order.total
)

fun OrderItemEntity.toModel() = OrderItem(productId, productName, quantity, price, notes)

fun Order.toEntity() = OrderEntity(id, tableNumber, status, timestamp, total)
fun OrderItem.toEntity(orderId: String) = OrderItemEntity(
    orderId = orderId,
    productId = productId,
    productName = productName,
    quantity = quantity,
    price = price,
    notes = notes
)

fun com.example.arenaysol.data.model.WristbandSale.toEntity() = com.example.arenaysol.data.local.entity.WristbandSaleEntity(
    id = id,
    type = type,
    price = price,
    quantity = quantity,
    timestamp = timestamp,
    sellerId = sellerId
)
