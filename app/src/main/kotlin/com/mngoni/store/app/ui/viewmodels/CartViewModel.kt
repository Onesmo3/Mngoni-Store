package com.mngoni.store.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mngoni.store.core.CartLine
import com.mngoni.store.core.CartPricing
import com.mngoni.store.core.CartRepository
import com.mngoni.store.core.CatalogRepository
import com.mngoni.store.core.Money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel(
    private val cart: CartRepository,
    catalog: CatalogRepository,
) : ViewModel() {

    val lines: StateFlow<List<CartLine>> = combine(
        cart.observeItems(),
        catalog.observeProducts(),
    ) { items, products ->
        items.mapNotNull { item ->
            products.find { it.id == item.productId }?.let { CartLine(it, item.quantity) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val subtotal: StateFlow<Money> = lines.map { CartPricing.subtotal(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Money.ZERO)

    val deliveryFee: StateFlow<Money> = lines.map { CartPricing.deliveryFee(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Money.ZERO)

    val total: StateFlow<Money> = lines.map { CartPricing.total(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Money.ZERO)

    fun add(productId: String) = viewModelScope.launch { cart.add(productId) }

    fun setQuantity(productId: String, quantity: Int) =
        viewModelScope.launch { cart.setQuantity(productId, quantity) }

    fun remove(productId: String) = viewModelScope.launch { cart.remove(productId) }
}
