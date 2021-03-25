package com.embedded.di.module

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.embedded.MyApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MyApp) {

    @Provides @Singleton
   fun provideAppContext(): Context {
        return application
    }
    @Provides @Singleton
    fun provideSharedPref():SharedPreferences{
        return application.getSharedPreferences("MeetingPref",Context.MODE_PRIVATE)
    }

    @Provides
    fun getResources(context:Context): Resources? {
        return context.resources
    }

}