package com.embedded.di.component

import com.embedded.di.module.ActivityModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Krishan on 04/24/2020
 */

@Singleton
@Component(modules = arrayOf(
                ActivityModule::class
        )
)
interface ActivityComponent {
   // fun inject(loginActivity: LoginActivity)

}