package com.victor.ko.shopping_cart.models

import com.google.gson.annotations.SerializedName

data class PINSuccess(
    @SerializedName("login")
    val login: String?,
    @SerializedName("pin")
    val pin: String?,
    @SerializedName("sms_id")
    val smsId: Int?,
    @SerializedName("sms_response")
    val smsResponse: String?,
    @SerializedName("token")
    val token: String?
)