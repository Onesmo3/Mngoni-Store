package com.mngoni.store.payments

import retrofit2.http.Body
import retrofit2.http.POST

interface MpesaApi {
    @POST("stkPush")
    suspend fun stkPush(@Body request: StkPushRequest): StkPushResponse
}
