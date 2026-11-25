package com.mngoni.store.core

import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun observeProducts(): Flow<List<Product>>
    suspend fun refresh(): Result<Unit>
    suspend fun findById(id: String): Product?
}

interface CartRepository {
    fun observeItems(): Flow<List<CartItem>>
    suspend fun add(productId: String, quantity: Int = 1)
    suspend fun setQuantity(productId: String, quantity: Int)
    suspend fun remove(productId: String)
    suspend fun clear()
}

interface OrderRepository {
    fun observeOrders(): Flow<List<Order>>
    suspend fun placeOrder(order: Order)
    suspend fun markInitiated(orderId: String, checkoutRequestId: String)
    suspend fun markFailed(orderId: String)
}
