package com.victor.ko.scart.models

import com.google.gson.annotations.SerializedName

data class Catalog(
    @SerializedName("success")
    val success: CatalogSuccess?,
    @SerializedName("error")
    val error: Error?
)