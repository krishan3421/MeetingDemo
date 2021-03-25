package com.embedded.data.cal

import com.embedded.model.CalendarData
import com.embedded.model.MeetingItem


/**
 * Created by Krishan on 04/24/2020.
 */
interface CalRepository {
    suspend fun isEventExist(meetingItem: MeetingItem):Boolean
    suspend fun saveCalendarEvent(meetingItem: MeetingItem):String
    suspend fun getCalEvents():MutableList<CalendarData>
}