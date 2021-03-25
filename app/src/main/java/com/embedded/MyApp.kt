package com.embedded

import android.app.Application
import com.embedded.di.ComponentInjector
import com.embedded.di.component.AppComponent
import com.embedded.model.MeetingItem
import com.embedded.model.register.UserRegResponse


class MyApp :Application() {
    lateinit var  mAppComponent: AppComponent

     var token:String?=null
    var userRegResponse:UserRegResponse?=null
     var meetingList = mutableListOf<MeetingItem>()
    companion object{
        val TAG = MyApp::class.java.simpleName
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mAppComponent =   ComponentInjector.init(this)

    }
    fun getAppComponent(): AppComponent? {
        return mAppComponent
    }
}