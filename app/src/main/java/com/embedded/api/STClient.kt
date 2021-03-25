package com.embedded.api


import com.embedded.Constant
import com.embedded.model.MeetingItem
import retrofit2.Call
import retrofit2.http.GET

interface STClient {
    @GET(Constant.SERVICE_API.AGENT)
    fun getMeetingList(): Call<MutableList<MeetingItem>>
}