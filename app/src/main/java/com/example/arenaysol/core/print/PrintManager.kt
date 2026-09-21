package com.example.arenaysol.core.print

import com.example.arenaysol.data.model.Order
import java.nio.charset.Charset

object EscPos {
    val INIT = byteArrayOf(0x1B, 0x40)
    val ALIGN_LEFT = byteArrayOf(0x1B, 0x61, 0x00)
    val ALIGN_CENTER = byteArrayOf(0x1B, 0x61, 0x01)
    val ALIGN_RIGHT = byteArrayOf(0x1B, 0x61, 0x02)
    val BOLD_ON = byteArrayOf(0x1B, 0x45, 0x01)
    val BOLD_OFF = byteArrayOf(0x1B, 0x45, 0x00)
    val FEED_LINE = byteArrayOf(0x0A)
    val CUT = byteArrayOf(0x1D, 0x56, 0x41, 0x10)
}

class PrintManager {

    fun generateReceipt(order: Order): ByteArray {
        val bytes = mutableListOf<Byte>()

        bytes.addAll(EscPos.INIT.toList())
        bytes.addAll(EscPos.ALIGN_CENTER.toList())
        bytes.addAll(EscPos.BOLD_ON.toList())
        bytes.addAll("ARENA Y SOL\n".toByteArray(Charset.forName("GBK")).toList())
        bytes.addAll(EscPos.BOLD_OFF.toList())
        bytes.addAll("Mesa: ${order.tableNumber}\n".toByteArray().toList())
        bytes.addAll("Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.timestamp)}\n".toByteArray().toList())
        bytes.addAll(EscPos.FEED_LINE.toList())

        bytes.addAll(EscPos.ALIGN_LEFT.toList())
        order.items.forEach { item ->
            val line = "${item.productName.take(20).padEnd(20)} x${item.quantity} $${item.price * item.quantity}\n"
            bytes.addAll(line.toByteArray().toList())
        }

        bytes.addAll(EscPos.FEED_LINE.toList())
        bytes.addAll(EscPos.ALIGN_RIGHT.toList())
        bytes.addAll(EscPos.BOLD_ON.toList())
        bytes.addAll("TOTAL: $${order.total}\n".toByteArray().toList())
        bytes.addAll(EscPos.BOLD_OFF.toList())
        bytes.addAll(EscPos.FEED_LINE.toList())
        bytes.addAll(EscPos.FEED_LINE.toList())
        bytes.addAll(EscPos.CUT.toList())

        return bytes.toByteArray()
    }
}
