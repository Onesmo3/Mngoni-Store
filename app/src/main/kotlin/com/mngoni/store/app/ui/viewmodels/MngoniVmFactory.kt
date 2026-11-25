package com.mngoni.store.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mngoni.store.app.AppContainer
import com.mngoni.store.core.OrderRepository

class MngoniVmFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(CatalogViewModel::class.java) ->
            CatalogViewModel(container.catalogRepository) as T
        modelClass.isAssignableFrom(CartViewModel::class.java) ->
            CartViewModel(container.cartRepository, container.catalogRepository) as T
        modelClass.isAssignableFrom(CheckoutViewModel::class.java) ->
            CheckoutViewModel(
                container.cartRepository,
                container.catalogRepository,
                container.orderRepository,
                container.mpesaClient,
            ) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}

class OrderStatusVmFactory(
    private val orderId: String,
    private val orders: OrderRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        OrderStatusViewModel(orderId, orders) as T
}
