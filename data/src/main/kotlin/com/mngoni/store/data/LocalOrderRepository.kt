package com.mngoni.store.data

import com.mngoni.store.core.Order
import com.mngoni.store.core.OrderRepository
import com.mngoni.store.core.OrderStatus
import com.mngoni.store.core.PaymentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalOrderRepository(private val store: LocalStore) : OrderRepository {

    override fun observeOrders(): Flow<List<Order>> = store.observeOrders()

    override suspend fun placeOrder(order: Order) = store.saveOrder(order)

    override suspend fun markInitiated(orderId: String, checkoutRequestId: String) {
        update(orderId) {
            it.copy(
                checkoutRequestId = checkoutRequestId,
                paymentStatus = PaymentStatus.INITIATED,
            )
        }
    }

    override suspend fun markFailed(orderId: String) {
        update(orderId) {
            it.copy(
                status = OrderStatus.FAILED,
                paymentStatus = PaymentStatus.FAILED,
            )
        }
    }

    private suspend fun update(orderId: String, transform: (Order) -> Order) {
        val current = store.observeOrders().first()
        current.find { it.id == orderId }?.let { store.saveOrder(transform(it)) }
    }
}
