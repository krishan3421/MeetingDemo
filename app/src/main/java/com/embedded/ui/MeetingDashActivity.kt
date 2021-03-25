package com.embedded.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.embedded.MyApp
import com.embedded.R
import com.embedded.Validation
import com.embedded.di.ComponentInjector
import com.embedded.model.MeetingItem
import com.embedded.utils.Util
import com.embedded.viewmodel.MeetingViewModel
import com.events.calendar.views.EventsCalendar
import kotlinx.android.synthetic.main.activity_dash_meeting.progress_bar
import kotlinx.android.synthetic.main.activity_meeting_list.*
import kotlinx.android.synthetic.main.content_dash_meeting.*
import java.text.SimpleDateFormat
import java.util.*

class MeetingDashActivity : BaseActivity(), EventsCalendar.Callback {

    private lateinit var viewModel: MeetingViewModel
    private var meetingList= mutableListOf<MeetingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_meeting)
        setSupportActionBar(toolbar)
        initView()

    }

    private fun initView() {
        //simpleCalendarView.addEvent(Calendar.getInstance())
        toolbar.title = getString(R.string.meeting_calendar)
        val today = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)
        simpleCalendarView.setSelectionMode(simpleCalendarView.SINGLE_SELECTION)
            .setToday(today)
            .setMonthRange(today, end)
            .setWeekStartDay(Calendar.MONDAY, false)
            .setIsBoldTextOnSelectionEnabled(true)
            .setCallback(this)
            .build()
        initData()
    }

    private fun initData() {
        viewModel = createViewModel()
        attachObserver()
        callMeetingService()
    }

    private fun callMeetingService(){
        if(Validation.isOnline(this)){
            viewModel.getMeeting()
        }else{
            showToast("Internet is not connected,Please tyr again later")
        }
    }

    fun createMeeting(view:View){
        try{

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun attachObserver() {
        viewModel.isLoading.observe(this, Observer<Boolean> {
            it?.let { showLoadingDialog(it) }
        })
        viewModel.apiError.observe(this, Observer<String> {
            println("error>>>>>>>>>> $it")
            it?.let { showSnackBar(progress_bar,it) }
        })

        viewModel.updateMeetingResponse.observe(this, Observer<MutableList<MeetingItem>> {
            println("message>>>>>>>>>> ${Util.getStringFromBean(it)}")
           it.forEachIndexed { index, meetingItem ->
               meetingItem.startDate?.let {strDate->
                   val date: Date = SimpleDateFormat("yyyy-MM-dd").parse(strDate)
                   println("date>>>>>>>>>> $date")
                   val cal = Calendar.getInstance()
                   cal.time=date
                   //sout
                   simpleCalendarView.addEvent(cal)
               }

           }
            meetingList=it
            simpleCalendarView.build()
        })
    }

    private fun createViewModel(): MeetingViewModel =
        ViewModelProvider(this).get(MeetingViewModel::class.java).also {
            ComponentInjector.component.inject(it)
        }

    private fun showLoadingDialog(show: Boolean) {
        if (show) progress_bar.visibility = View.VISIBLE else progress_bar.visibility = View.GONE
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {

    }

    override fun onDaySelected(selectedDate: Calendar?) {
        val date: Date? = selectedDate?.time
        val format = SimpleDateFormat("yyyy-MM-dd")
        val strSelectedDate = format.format(date)
        println("filterList>>>>>>>>>> ${Util.getStringFromBean(strSelectedDate)}")
         var filterList= mutableListOf<MeetingItem>()
        filterList=meetingList.filter { meetingItem -> meetingItem.startDate==strSelectedDate }.toMutableList()
        if(filterList.isNotEmpty()){
            MyApp.instance.meetingList=filterList
            goToMeetingListActivity()
        }else{
           showToast("Sorry!,No event found on this Date.")
        }
        //println("filterList>>>>>>>>>> ${Util.getStringFromBean(filterList)}")
    }

    private fun goToMeetingListActivity(){
        val intent = Intent(this,MeetingListActivity::class.java)
        //intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onMonthChanged(monthStartDate: Calendar?) {

    }
}