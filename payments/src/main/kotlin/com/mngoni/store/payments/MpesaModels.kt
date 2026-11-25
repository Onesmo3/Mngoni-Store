package com.mngoni.store.payments

import kotlinx.serialization.Serializable

@Serializable
data class StkPushRequest(
    val phone: String,
    val amount: Long,
    val orderId: String,
    val description: String,
)

@Serializable
data class StkPushResponse(
    val checkoutRequestId: String? = null,
    val responseCode: String? = null,
    val responseDescription: String? = null,
    val customerMessage: String? = null,
)

sealed interface PaymentRequestResult {
    data class Sent(val checkoutRequestId: String, val message: String) : PaymentRequestResult
    data class Rejected(val message: String) : PaymentRequestResult
    data class Error(val message: String) : PaymentRequestResult
}
