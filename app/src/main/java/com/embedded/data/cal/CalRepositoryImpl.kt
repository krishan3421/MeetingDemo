package com.embedded.data.cal


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.embedded.Constant
import com.embedded.api.STClient
import com.embedded.model.CalendarData
import com.embedded.model.MeetingItem
import com.embedded.utils.Util
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.util.*


/**
 * Created by Krishan on 04/24/2020.
 */

class CalRepositoryImpl(private val context: Context) : CalRepository {

    companion object{
        private val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events.CALENDAR_ID,//0
            CalendarContract.Events.TITLE,//1
            CalendarContract.Events.DESCRIPTION,//2
            CalendarContract.Events.DTSTART,//3
            CalendarContract.Events.DTEND,//4
            CalendarContract.Events.EVENT_LOCATION//5
        )
    }

    override suspend fun isEventExist(meetingItem: MeetingItem): Boolean {
        var isExist=false
        try {
            var strDate :Long= 1615464487000//Calendar.getInstance().timeInMillis// starting time in milliseconds
            var endDate :Long=1615464487000//Calendar.getInstance().timeInMillis// ending time in milliseconds
            var title = "${"\"${meetingItem.name}\""}\""          //"\"kriTest\""
            val cur: Cursor? =
            CalendarContract.Instances.query(context.contentResolver, EVENT_PROJECTION, strDate, endDate, title);
            if (cur!!.count > 0) {
                isExist = true
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return  isExist
    }

    override suspend fun saveCalendarEvent(meetingItem: MeetingItem): String {
       var eventId :String = ""
        try {
            val calID: Long = Constant.CALENDAR.CALENDAR_ID
            val startMillis: Long = meetingItem.calendar.run {
                this!!.timeInMillis
            }
            val endMillis: Long =meetingItem.calendar.run {
                this!!.timeInMillis
            }

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, meetingItem.name)
                put(CalendarContract.Events.DESCRIPTION, "Group workout")
                put(CalendarContract.Events.CALENDAR_ID, calID)
                put(CalendarContract.Events.EVENT_TIMEZONE, "India/New_Delhi")
            }
            val uri: Uri? = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

           // get the event ID that is the last element in the Uri
            uri?.lastPathSegment?.let {
                eventId=it
                println("event id>>>>>>> $eventId")
            }
            //
            // ... do something with event ID
            //
            //
        }catch (e:Exception){
            e.printStackTrace()
        }
        return eventId
    }

    override suspend  fun getCalEvents(): MutableList<CalendarData> {
       val listData = mutableListOf<CalendarData>()
        try {
            val uri: Uri = CalendarContract.Events.CONTENT_URI
            val selection: String = "((${CalendarContract.Events.CALENDAR_ID} = ?))"
            val selectionArgs: Array<String> = arrayOf("${Constant.CALENDAR.CALENDAR_ID}")
            val cur: Cursor? = context.contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
            // Use the cursor to step through the returned records
            cur?.let {
                while (it.moveToNext()) {
                    // Get the field values
                    val calID: Long = it.getLong(0)
                    val title: String = it.getString(1)
                    val description: String = it.getString(3)
                    val dtstart: Long = it.getLong(3)
                    val dtend: Long = it.getLong(4)
                   // println("title>>>>>>>>$title")
                   // println("datestart>>>>>>>>$dtstart")
                    val eventLocation: String = it.getString(5)
                    var calendarData=CalendarData(calID,title,description,Util.convertLongToTime(dtstart),
                        Util.convertLongToTime(dtend),eventLocation)
                    // val ownerName: String = it.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
                   // println("data>>>>>> $calID $title -- ${Util.convertLongToTime(dtstart)}")
                    listData.add(calendarData)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return listData
    }


}
