package com.mngoni.store.data

import com.mngoni.store.core.CartItem
import com.mngoni.store.core.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalCartRepository(
    private val store: LocalStore,
) : CartRepository {

    companion object {
        const val MAX_QUANTITY = 20
    }

    override fun observeItems(): Flow<List<CartItem>> = store.observeCart()

    override suspend fun add(productId: String, quantity: Int) {
        if (quantity <= 0) return
        val current = store.observeCart().first().toMutableList()
        val index = current.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(
                quantity = (existing.quantity + quantity).coerceAtMost(MAX_QUANTITY),
            )
        } else {
            current += CartItem(productId, quantity.coerceAtMost(MAX_QUANTITY))
        }
        store.saveCart(current)
    }

    override suspend fun setQuantity(productId: String, quantity: Int) {
        val current = store.observeCart().first()
        val updated = if (quantity <= 0) {
            current.filterNot { it.productId == productId }
        } else {
            current.map {
                if (it.productId == productId) it.copy(quantity = quantity.coerceAtMost(MAX_QUANTITY)) else it
            }
        }
        store.saveCart(updated)
    }

    override suspend fun remove(productId: String) {
        store.saveCart(store.observeCart().first().filterNot { it.productId == productId })
    }

    override suspend fun clear() = store.saveCart(emptyList())
}
