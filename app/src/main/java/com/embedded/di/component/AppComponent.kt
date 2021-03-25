package com.embedded.di.component

import com.embedded.di.module.ApiModule
import com.embedded.di.module.AppModule
import com.embedded.di.module.STRepositoryModule
import com.embedded.viewmodel.LoginViewModel
import com.embedded.viewmodel.MeetingViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Krishan on 04/24/2020
 */

@Singleton
@Component(
        modules = [ApiModule::class, STRepositoryModule::class, AppModule::class]
)
interface AppComponent {
        fun inject(loginViewModel: LoginViewModel)
        fun inject(meetingViewModel: MeetingViewModel)

}