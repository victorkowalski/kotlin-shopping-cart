package com.victor.ko.shopping_cart.models

import com.google.gson.annotations.SerializedName

data class PIN(
    @SerializedName("success")
    val success: PINSuccess?,
    @SerializedName("error")
    val error: Error?
)
