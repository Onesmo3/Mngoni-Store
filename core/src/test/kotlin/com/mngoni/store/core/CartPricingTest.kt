package com.mngoni.store.core

import kotlin.test.Test
import kotlin.test.assertEquals

class CartPricingTest {

    private val shoes = Product(
        id = "p1",
        name = "Sneaker",
        category = Category.SHOES,
        price = Money(18_000),
        description = "",
        stock = 10,
    )

    @Test
    fun `empty cart has no fees`() {
        assertEquals(Money.ZERO, CartPricing.subtotal(emptyList()))
        assertEquals(Money.ZERO, CartPricing.deliveryFee(emptyList()))
        assertEquals(Money.ZERO, CartPricing.total(emptyList()))
    }

    @Test
    fun `delivery fee applies below threshold`() {
        val lines = listOf(CartLine(shoes, 1))
        assertEquals(Money(18_000), CartPricing.subtotal(lines))
        assertEquals(Money(5_000), CartPricing.deliveryFee(lines))
        assertEquals(Money(23_000), CartPricing.total(lines))
    }

    @Test
    fun `delivery is free at threshold`() {
        val lines = listOf(CartLine(shoes, 3)) // 54,000
        assertEquals(Money.ZERO, CartPricing.deliveryFee(lines))
        assertEquals(Money(54_000), CartPricing.total(lines))
    }
}
