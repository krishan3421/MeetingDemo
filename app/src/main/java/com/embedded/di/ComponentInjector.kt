package com.embedded.di

import com.embedded.MyApp
import com.embedded.di.component.AppComponent
import com.embedded.di.component.DaggerAppComponent
import com.embedded.di.module.AppModule


class ComponentInjector {

    companion object {

        lateinit var component: AppComponent

        fun init(application: MyApp) :AppComponent{
            component = DaggerAppComponent.builder()
                //.apiModule(ApiModule())
                 .appModule(AppModule(application))
                //.eBRepositoryModule(EBRepositoryModule())
                .build()

            return component
        }
    }
}

