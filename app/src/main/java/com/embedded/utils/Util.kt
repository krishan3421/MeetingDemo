package com.embedded.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.embedded.R
import com.google.gson.Gson
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun Toast.show(){

}
class Util {

    companion object{
        val TAG= Util::class.java.simpleName
        var mm_dd_yyyy = SimpleDateFormat("MMM dd yyyy")
        var yyyy_mm_dd: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var yyyy_mm_dd_hh_mm_ss: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var isShowing = false
         fun alertDialog(message:String, context: Context) {
            try {
                if (!isShowing) {
                    val builder1 = AlertDialog.Builder(context)
                    builder1.setTitle(context.getString(R.string.app_name))
                    builder1.setMessage(message)
                    builder1.setCancelable(false)
                    builder1.setPositiveButton(context.getString(R.string.ok)) { dialog, id ->
                         isShowing = false
                        dialog.cancel()
                    }.show()
                     isShowing = true
                 }
            }catch (e:Exception) {
                e.printStackTrace()
                e.message?.let { Log.e(TAG, it) }
            }
         }

        fun getStringFromBean(bean: Any?): String? {
            var result: String? = null
            try {
                result = Gson().toJson(bean)
            } catch (e: Exception) {
                e.message?.let { Log.e(TAG, it) }
                e.printStackTrace()
            }
            return result

        }

        fun latlongFormat(value: Double): String {
            var df: DecimalFormat? = null
            var result: String = "00.00"
            try {
                df = DecimalFormat("#.##########")
                df.roundingMode = RoundingMode.CEILING
                result = df.format(value)
            } catch (ex: Exception) {
                ex.printStackTrace()
                result = value.toString()
            }

            return result
        }

        fun getCurrentDate_MM_DD_YYYY():String?{
            var currDate: String? = null
            try {
                currDate = mm_dd_yyyy.format(Date())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return currDate
        }
        fun get_yyyy_mm_ddString(c: Calendar): String? {
            return yyyy_mm_dd.format(c.time)
        }

        fun getCurrentDate_yyyy_mm_dd():String{

            return yyyy_mm_dd.format(Date())
        }
        fun getCalendarInstance(dateStr:String):Calendar{
            val myDate= yyyy_mm_dd_hh_mm_ss.parse(dateStr)
            val cal = Calendar.getInstance()
            cal.time = myDate
            return cal
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            return yyyy_mm_dd_hh_mm_ss.format(date)
        }
    }



}