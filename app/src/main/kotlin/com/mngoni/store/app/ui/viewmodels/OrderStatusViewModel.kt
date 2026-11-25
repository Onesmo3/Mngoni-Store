package com.mngoni.store.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mngoni.store.core.Order
import com.mngoni.store.core.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class OrderStatusViewModel(
    orderId: String,
    orders: OrderRepository,
) : ViewModel() {
    val order: StateFlow<Order?> = orders.observeOrders()
        .map { list -> list.find { it.id == orderId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
