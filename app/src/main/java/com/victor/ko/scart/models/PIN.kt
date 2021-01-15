package com.victor.ko.scart.models

import com.google.gson.annotations.SerializedName

data class PIN(
    @SerializedName("success")
    val success: PINSuccess?,
    @SerializedName("error")
    val error: Error?
)
