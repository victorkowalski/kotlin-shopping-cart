package com.victor.ko.shopping_cart.models

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("error")
    val error: Int?,
    @SerializedName("message")
    val message: String?
)