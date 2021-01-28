package com.victor.ko.scart.network

import com.victor.ko.scart.models.Catalog
import com.victor.ko.scart.models.PIN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiClient {

    @Headers("mock:true")
    @GET("?route=api/mob_app&action=pin")
    suspend fun getPin(@Query("phone") phone: String, @Query("test") test: String = "0"
    ): Response<PIN>

    @Headers("mock:true")
    @GET("?route=api/mob_app&action=catalog")
    fun catalog(@Query("token") accessToken: String): Response<Catalog>
}