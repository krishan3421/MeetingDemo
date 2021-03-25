package com.embedded.model


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("iso_code")
    val isoCode: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone_prefix")
    val phonePrefix: String?
)