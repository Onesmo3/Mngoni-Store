package com.mngoni.store.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mngoni.store.core.CartLine
import com.mngoni.store.core.CartPricing
import com.mngoni.store.core.CartRepository
import com.mngoni.store.core.CatalogRepository
import com.mngoni.store.core.Order
import com.mngoni.store.core.OrderLine
import com.mngoni.store.core.OrderRepository
import com.mngoni.store.core.OrderStatus
import com.mngoni.store.core.PaymentStatus
import com.mngoni.store.payments.MpesaClient
import com.mngoni.store.payments.PaymentRequestResult
import com.mngoni.store.payments.PhoneNormalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val name: String = "",
    val phone: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val placedOrderId: String? = null,
)

class CheckoutViewModel(
    private val cart: CartRepository,
    private val catalog: CatalogRepository,
    private val orders: OrderRepository,
    private val mpesa: MpesaClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }

    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value, error = null) }

    fun placeOrder() {
        val current = _uiState.value
        if (current.isSubmitting) return

        val customerName = current.name.trim()
        if (customerName.isBlank()) {
            _uiState.update { it.copy(error = "Weka jina la mteja.") }
            return
        }
        val phone = PhoneNormalizer.normalize(current.phone)
        if (phone == null) {
            _uiState.update { it.copy(error = "Namba ya simu si sahihi. Tumia mfano 0712345678.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            val lines = buildLines()
            if (lines.isEmpty()) {
                _uiState.update { it.copy(isSubmitting = false, error = "Cart iko tupu.") }
                return@launch
            }

            val subtotal = CartPricing.subtotal(lines)
            val fee = CartPricing.deliveryFee(lines)
            val order = Order(
                id = "ORD-${System.currentTimeMillis()}",
                customerName = customerName,
                phone = phone,
                lines = lines.map { OrderLine(it.product.id, it.product.name, it.product.price, it.quantity) },
                subtotal = subtotal,
                deliveryFee = fee,
                total = CartPricing.total(lines),
                status = OrderStatus.PENDING,
                paymentStatus = PaymentStatus.NOT_STARTED,
                createdAt = System.currentTimeMillis(),
            )
            orders.placeOrder(order)

            when (val result = mpesa.requestStkPush(phone, order.total.shillings, order.id, "Mngoni Store ${order.id}")) {
                is PaymentRequestResult.Sent -> {
                    orders.markInitiated(order.id, result.checkoutRequestId)
                    cart.clear()
                    _uiState.update {
                        it.copy(isSubmitting = false, placedOrderId = order.id)
                    }
                }
                is PaymentRequestResult.Rejected -> {
                    orders.markFailed(order.id)
                    _uiState.update { it.copy(isSubmitting = false, error = result.message) }
                }
                is PaymentRequestResult.Error -> {
                    orders.markFailed(order.id)
                    _uiState.update { it.copy(isSubmitting = false, error = result.message) }
                }
            }
        }
    }

    private suspend fun buildLines(): List<CartLine> {
        val products = catalog.observeProducts().first()
        return cart.observeItems().first().mapNotNull { item ->
            products.find { it.id == item.productId }?.let { CartLine(it, item.quantity) }
        }
    }
}
