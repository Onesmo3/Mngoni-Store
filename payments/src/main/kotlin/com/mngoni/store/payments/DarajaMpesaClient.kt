package com.mngoni.store.payments

import com.mngoni.store.core.MngoniJson
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface MpesaClient {
    suspend fun requestStkPush(
        phone: String,
        amount: Long,
        orderId: String,
        description: String,
    ): PaymentRequestResult
}

class DarajaMpesaClient(
    baseUrl: String,
) : MpesaClient {

    private val api: MpesaApi = Retrofit.Builder()
        .baseUrl(baseUrl.ensureTrailingSlash())
        .addConverterFactory(
            MngoniJson.instance.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(MpesaApi::class.java)

    override suspend fun requestStkPush(
        phone: String,
        amount: Long,
        orderId: String,
        description: String,
    ): PaymentRequestResult = try {
        val response = api.stkPush(StkPushRequest(phone, amount, orderId, description))
        val code = response.responseCode
        val checkoutId = response.checkoutRequestId
        when {
            code == "0" && !checkoutId.isNullOrBlank() ->
                PaymentRequestResult.Sent(checkoutId, response.customerMessage ?: "Tuma malipo kwenye simu yako.")
            else ->
                PaymentRequestResult.Rejected(
                    response.responseDescription ?: response.customerMessage ?: "Ombi la malipo halikukubaliwa.",
                )
        }
    } catch (e: Exception) {
        PaymentRequestResult.Error(e.message ?: "Hakukuweza kuwasiliana na huduma ya malipo.")
    }

    private fun String.ensureTrailingSlash(): String = if (endsWith("/")) this else "$this/"
}
