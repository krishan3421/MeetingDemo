package com.embedded.model


import com.google.gson.annotations.SerializedName
import java.util.*

data class MeetingItem(
    @SerializedName("email")
    val email: String?="",
    @SerializedName("meetingid")
    val meetingid: String?="",
    @SerializedName("name")
    val name: String?="",
    @SerializedName("phone")
    val phone: String?="",
    @SerializedName("product")
    val product: String?="",
    @SerializedName("roomhosturl")
    val roomhosturl: String?="",
    @SerializedName("roomhosturl_forced")
    val roomhosturlForced: String?="",
    @SerializedName("roomurl")
    val roomurl: String?="",
    @SerializedName("roomurl_forced")
    val roomurlForced: String?="",
    @SerializedName("startDate")
    val startDate: String?="",
    @SerializedName("startTime")
    val startTime: String?="",
    @SerializedName("calendar")
    var calendar: Calendar?=null
)