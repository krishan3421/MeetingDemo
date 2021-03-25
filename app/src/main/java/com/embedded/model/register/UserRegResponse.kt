package com.embedded.model.register


import com.google.gson.annotations.SerializedName

data class UserRegResponse(
    @SerializedName("token")
    val token: String?,
    @SerializedName("userId")
    val userId: String?
)