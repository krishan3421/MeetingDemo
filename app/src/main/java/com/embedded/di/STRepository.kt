package com.embedded.di

import com.embedded.model.MeetingItem


/**
 * Created by Krishan on 04/24/2020.
 */
interface STRepository {
    fun getMeetingList(successHandler: (MutableList<MeetingItem>) -> Unit, failureHandler: (String?) -> Unit)
}