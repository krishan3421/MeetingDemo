package com.embedded.di


import com.embedded.api.STClient
import com.embedded.model.MeetingItem
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response


/**
 * Created by Krishan on 04/24/2020.
 */

class STRepositoryImpl(private val apiService: STClient) : STRepository {

    companion object{
        private const val ERROR_500 :String="Unable to connect to server. Please try again after sometime."
    }



    override fun getMeetingList(successHandler: (MutableList<MeetingItem>) -> Unit, failureHandler: (String?) -> Unit) {
        apiService.getMeetingList().enqueue(object:retrofit2.Callback<MutableList<MeetingItem>>{

            override fun onResponse(call: Call<MutableList<MeetingItem>>, response: Response<MutableList<MeetingItem>>) {
                // println("responseurl>>>>>>> ${response.headers().})
                if(response?.body()!=null){
                    response?.body()?.let {
                        successHandler(it)
                    }
                }else{
                    if(response.code() ==500){
                        failureHandler(ERROR_500)
                    }else{
                        failureHandler("Error Code ${response.code()}")
                    }
                }

            }

            override fun onFailure(call: Call<MutableList<MeetingItem>>, t: Throwable) {
                println("error>>>> ${Gson().toJson(t)}")
                failureHandler(t.message)
            }

        })
    }


}
