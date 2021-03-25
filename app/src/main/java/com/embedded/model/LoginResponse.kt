package com.embedded.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("expires")
    val expires: Int?,
    @SerializedName("refresh")
    val refresh: String?,
    @SerializedName("token")
    val token: String?
)