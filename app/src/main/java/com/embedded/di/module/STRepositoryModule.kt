package com.embedded.di.module

import android.content.Context
import com.embedded.api.STClient
import com.embedded.data.cal.CalRepository
import com.embedded.data.cal.CalRepositoryImpl
import com.embedded.di.STRepository
import com.embedded.di.STRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Krishan on 04/24/2020
 */
@Module
class STRepositoryModule {

    @Provides @Singleton
    fun providePostRepository(apiService: STClient): STRepository =
        STRepositoryImpl(apiService)

    @Provides @Singleton
    fun provideCalRepository(context: Context): CalRepository =
        CalRepositoryImpl(context)
}