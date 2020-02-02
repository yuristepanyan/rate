package com.rate.am.net

import com.rate.am.model.BankDetailResponse
import com.rate.am.model.RatesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("rates.ashx")
    fun getRatesAsync(): Deferred<RatesResponse>

    @GET("branches.ashx")
    fun getBankDetailAsync(@Query("id") id: String): Deferred<BankDetailResponse>
}