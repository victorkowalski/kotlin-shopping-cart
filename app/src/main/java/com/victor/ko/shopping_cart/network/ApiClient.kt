package com.victor.ko.shopping_cart.network

import com.victor.ko.shopping_cart.models.PIN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiClient {
    //@GET("/api/breeds/image/random")
    //suspend fun getRandomDogImage(): Response<DogImageModel>

    @Headers("mock:true")
    @GET("?route=api/mob_app&action=pin")
    suspend fun getPin(@Query("phone") phone: String, @Query("test") test: String = "0"
    ): Response<PIN>
}