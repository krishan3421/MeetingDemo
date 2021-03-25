package com.embedded.di.module


import com.google.gson.GsonBuilder
import com.embedded.Constant
import com.embedded.api.HttpClientService
import com.embedded.api.STClient
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Krishan on 04/24/2020
 */
@Module
class ApiModule {
    @Provides @Singleton
    fun provideApiService(): STClient {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
                .baseUrl(Constant.WEBSERVICE.RESTSERVICEURL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(STClient::class.java)
    }


}