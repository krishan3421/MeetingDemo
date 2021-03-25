package com.embedded.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.embedded.di.STRepository
import com.embedded.model.LoginReq
import com.embedded.model.LoginResponse
import com.embedded.model.email.ChangeEmailReq
import com.embedded.model.phone.ChangePhoneReq
import com.embedded.model.register.UserRegReq
import com.embedded.model.register.UserRegResponse
import javax.inject.Inject

class LoginViewModel :ViewModel(){

    @Inject
    lateinit var repository: STRepository

    var isLoading = MutableLiveData<Boolean>()

    var apiError = MutableLiveData<String>()

    var loginResponse = MutableLiveData<LoginResponse>()
    var userRegister = MutableLiveData<UserRegResponse>()
    var closeAccountResponse = MutableLiveData<String>()
    var changeEmailResponse = MutableLiveData<String>()
    var changePhoneResponse = MutableLiveData<String>()
    var changeUserHostResponse = MutableLiveData<String>()
    var updateDateResponse = MutableLiveData<String>()


}