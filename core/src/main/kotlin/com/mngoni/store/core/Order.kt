package com.mngoni.store.core

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING, PAID, FAILED, CANCELLED,
}

@Serializable
enum class PaymentStatus {
    NOT_STARTED, INITIATED, CONFIRMED, FAILED,
}

@Serializable
data class OrderLine(
    val productId: String,
    val name: String,
    val unitPrice: Money,
    val quantity: Int,
) {
    val subtotal: Money get() = unitPrice * quantity
}

@Serializable
data class Order(
    val id: String,
    val customerName: String,
    val phone: String,
    val lines: List<OrderLine>,
    val subtotal: Money,
    val deliveryFee: Money,
    val total: Money,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val checkoutRequestId: String? = null,
    val createdAt: Long,
)
