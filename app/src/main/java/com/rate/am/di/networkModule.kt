package com.rate.am.di

import android.content.Context
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rate.am.BuildConfig
import com.rate.am.deserialization.BankDetailResponseDeserializer
import com.rate.am.deserialization.RatesResponseDeserializer
import com.rate.am.model.BankDetailResponse
import com.rate.am.model.RatesResponse
import com.rate.am.net.RatesApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { gson() }

    single { interceptor() }

    single { httpClient(get(), cache(androidContext())) }

    single { retrofit(get(), get()) }

    single { ratesApi(get()) }

    single { glide(androidContext()) }
}

private fun gson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(RatesResponse::class.java, RatesResponseDeserializer())
        .registerTypeAdapter(BankDetailResponse::class.java, BankDetailResponseDeserializer())
        .create()
}

private fun interceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

private fun cache(context: Context) = Cache(
    File(context.cacheDir, BuildConfig.CACHE_DIR),
    (10 * 1024 * 1024).toLong()
)

private fun httpClient(interceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
    builder.readTimeout(60, TimeUnit.SECONDS)
    builder.connectTimeout(30, TimeUnit.SECONDS)

    builder.addInterceptor { chain ->
        val requestBuilder = chain.request()
            .newBuilder()

        requestBuilder.addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")


        val request = requestBuilder.build()
        chain.proceed(request)
    }
        .cache(cache)

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(interceptor)
    }

    return builder.build()
}

private fun retrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

private fun ratesApi(retrofit: Retrofit) = retrofit.create(RatesApi::class.java)

private fun glide(context: Context) = Glide.with(context)