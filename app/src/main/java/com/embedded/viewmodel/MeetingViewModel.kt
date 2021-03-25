package com.embedded.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embedded.data.cal.CalRepository
import com.embedded.di.STRepository
import com.embedded.model.CalendarData
import com.embedded.model.LoginReq
import com.embedded.model.LoginResponse
import com.embedded.model.MeetingItem
import com.embedded.model.email.ChangeEmailReq
import com.embedded.model.phone.ChangePhoneReq
import com.embedded.model.register.UserRegReq
import com.embedded.model.register.UserRegResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class MeetingViewModel :ViewModel(){

    @Inject
    lateinit var repository: STRepository

    @Inject
    lateinit var calRepository: CalRepository
    var isLoading = MutableLiveData<Boolean>()

    var apiError = MutableLiveData<String>()

    var loginResponse = MutableLiveData<LoginResponse>()
    var updateMeetingResponse = MutableLiveData<MutableList<MeetingItem>>()
    var calListResponse = MutableLiveData<MutableList<CalendarData>>()
    var eventIdResponse = MutableLiveData<String>()
    fun getMeeting() {
        isLoading.value = true
        repository.getMeetingList(
            {
                updateMeetingResponse.value = it
                isLoading.value = false
            },

            {
                apiError.value = it
                isLoading.value = false
            })
    }

    fun isEventExist(meetingItem: MeetingItem){
        viewModelScope.launch {
            var isExist=   calRepository.isEventExist(meetingItem)
            println("isExist>>>>>>>>> $isExist")
        }
    }

    fun saveCalendarEvent(meetingItem: MeetingItem){
        isLoading.value = true
        viewModelScope.launch {
            val eventId:String=   calRepository.saveCalendarEvent(meetingItem)
            eventIdResponse.value=eventId
            isLoading.value = false
        }
    }

    fun getCalList(){
        isLoading.value = true
        var list = mutableListOf<CalendarData>()
        viewModelScope.launch {
            list=   calRepository.getCalEvents()
            calListResponse.value=list
            isLoading.value = false
        }
    }
}