package com.mngoni.store.core

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(val productId: String, val quantity: Int)

data class CartLine(val product: Product, val quantity: Int) {
    val subtotal: Money get() = product.price * quantity
}

object CartPricing {
    val FREE_DELIVERY_THRESHOLD = Money(50_000)
    val DELIVERY_FEE = Money(5_000)

    fun subtotal(lines: List<CartLine>): Money =
        lines.fold(Money.ZERO) { acc, line -> acc + line.subtotal }

    fun deliveryFee(lines: List<CartLine>): Money =
        if (lines.isEmpty() || subtotal(lines) >= FREE_DELIVERY_THRESHOLD) Money.ZERO else DELIVERY_FEE

    fun total(lines: List<CartLine>): Money = subtotal(lines) + deliveryFee(lines)
}
