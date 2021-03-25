package com.embedded.model

data class CalendarData(val calId:Long,val title:String="",val description:String="",
                        val startDate:String="",val endDate:String="",var eventLocation:String="")