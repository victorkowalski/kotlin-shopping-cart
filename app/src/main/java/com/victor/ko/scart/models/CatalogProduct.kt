package com.victor.ko.scart.models

import com.google.gson.annotations.SerializedName

data class CatalogProduct(
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url_parameters")
    val urlParameters: String?
)