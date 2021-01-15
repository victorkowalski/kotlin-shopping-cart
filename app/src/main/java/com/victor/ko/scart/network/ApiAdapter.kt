package com.victor.ko.scart.network

import com.victor.ko.scart.utils.MockRequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(MockRequestInterceptor())
            .build()

    val apiClient: ApiClient = Retrofit.Builder()
        .baseUrl("https://dog.ceo")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)
}