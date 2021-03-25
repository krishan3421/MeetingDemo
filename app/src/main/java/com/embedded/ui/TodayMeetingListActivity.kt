package com.embedded.ui

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.embedded.Constant
import com.embedded.R
import com.embedded.Validation
import com.embedded.di.ComponentInjector
import com.embedded.model.CalendarData
import com.embedded.model.MeetingItem
import com.embedded.ui.adapter.MeetingAdapter
import com.embedded.utils.Util
import com.embedded.viewmodel.MeetingViewModel
import kotlinx.android.synthetic.main.activity_today_meeting_list.*
import kotlinx.android.synthetic.main.content_today_meeting_list.*
import java.util.*

class TodayMeetingListActivity : BaseActivity() {

    private lateinit var viewModel: MeetingViewModel
    private var meetingList= mutableListOf<MeetingItem>()
    private lateinit var meetingAdapter: MeetingAdapter
    companion object{
        private val CALENDAR_REQUEST_CODE = 101
        private val TAG=TodayMeetingListActivity::class.java.simpleName
        // The indices for the projection array above.
        private const val PROJECTION_ID_INDEX: Int = 0
        private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
        private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
        private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3
    }
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Events.CALENDAR_ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DESCRIPTION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.EVENT_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_meeting_list)
        setSupportActionBar(toolbar)
       // getAllEvent()
        initData()
        initView()

    }
    private fun initData() {
        viewModel = createViewModel()
        var str ="kriTest";
        println(">>>>>>>>>>>>>> ${"\"$str\""}")
        attachObserver()
        isEventExist()
        callGetCalList()
    }
    private fun initView() {
        //toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_action_back)
        //toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = getString(R.string.today_meeting)//"${rsLoginResponse?.data?.name}"
        meetingAdapter= MeetingAdapter(this,meetingList)
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TodayMeetingListActivity)
            adapter = meetingAdapter
        }
        showHideRecycler()
        swap_refresh.setOnRefreshListener {
            callMeetingService()
        }
        save_button.setOnClickListener {
            viewModel.saveCalendarEvent(MeetingItem())
        }

        callMeetingService()
    }

    private fun callMeetingService(){
        if(Validation.isOnline(this)){
            viewModel.getMeeting()
        }else{
            showToast("Internet is not connected,Please tyr again later")
            if(swap_refresh.isRefreshing){
                swap_refresh.isRefreshing=false
            }

        }
    }

    private fun attachObserver() {
        viewModel.isLoading.observe(this, Observer<Boolean> {
            it?.let { showLoadingDialog(it) }
        })
        viewModel.apiError.observe(this, Observer<String> {
            println("error>>>>>>>>>> $it")
            it?.let { showSnackBar(progress_bar,it) }
            if(swap_refresh.isRefreshing){
                swap_refresh.isRefreshing=false
            }
        })

        viewModel.updateMeetingResponse.observe(this, Observer<MutableList<MeetingItem>> {
            println("message>>>>>>>>>> ${Util.getStringFromBean(it)}")
            if(swap_refresh.isRefreshing){
                swap_refresh.isRefreshing=false
            }
            if(it.isNotEmpty()){
                it.forEachIndexed { index, meetingItem ->
                    val calendar =Util.getCalendarInstance("${meetingItem!!.startDate} ${meetingItem!!.startTime}")
                    meetingItem.calendar = calendar
                    // it[index] = meetingItem
                    //saveEventOnCalendar(meetingItem)
                }
                val myList = it.filter { meetingItem -> meetingItem.startDate == Util.getCurrentDate_yyyy_mm_dd() }.toList()
                println("filter list>>>>>>>>>> ${Util.getStringFromBean(myList)}")
                if(myList.isNotEmpty()){
                    meetingAdapter.addAll(myList.toMutableList())
                }
            }
            showHideRecycler()
        })
        viewModel.calListResponse.observe(this,Observer<MutableList<CalendarData>>{
            println("cal listdata>>>>>> ${Util.getStringFromBean(it)}")
        })

        viewModel.eventIdResponse.observe(this,Observer<String>{
            println("eventId>>>>>> $it")
        })
    }

    private fun showHideRecycler(){
        if(meetingAdapter.itemCount >0){
            recycler_view.visibility = View.VISIBLE
            no_meeting_text.visibility=View.GONE
        }else{
            recycler_view.visibility = View.GONE
            no_meeting_text.visibility=View.VISIBLE
        }
    }

    private fun createViewModel(): MeetingViewModel =
        ViewModelProvider(this).get(MeetingViewModel::class.java).also {
            ComponentInjector.component.inject(it)
        }

    override fun onResume() {
        super.onResume()
        meetingAdapter.setOnItemClickListener(object :MeetingAdapter.MeetingClickListener{
            override fun onItemClick(meetingItem: MeetingItem, v: View) {
                if(meetingItem.roomhosturlForced.isNullOrEmpty()){
                    showToast("Meeting Url is not found")
                }else {
                    goToMeetingListActivity(meetingItem.roomhosturlForced!!)
                }
            }

        })
    }

    private fun goToMeetingListActivity(url:String){
        val intent = Intent(this,WebViewActivity::class.java)
        // intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(WebViewActivity.MEETING_URL,url)
        startActivity(intent)
    }

    fun createMeeting(view: View) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showLoadingDialog(show: Boolean) {
        if (show) progress_bar.visibility = View.VISIBLE else progress_bar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_today, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_calendar ->{
                goToMeetingCalendarActivity()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToMeetingCalendarActivity(){
        val intent = Intent(this,MeetingDashActivity::class.java)
        // intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setupPermissions() {
        val permissionWrite = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_CALENDAR)
        val permissionRead = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CALENDAR)

        if (permissionWrite != PackageManager.PERMISSION_GRANTED && permissionRead != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }else{
            //saveEventOnCalendar()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR),
            CALENDAR_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALENDAR_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                }else {
                    Log.i(TAG, "Permission has been granted by user")
                    //saveEventOnCalendar()
                }
            }
        }
    }


    private fun saveEventOnCalendar(meetingItem: MeetingItem){
        //https://developer.android.com/guide/topics/providers/calendar-provider
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
        val uri: Uri? = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

// get the event ID that is the last element in the Uri
        uri?.lastPathSegment?.let {
            val eventID: Long = it.toLong()
            println("event id>>>>>>> $eventID")
        }
//
// ... do something with event ID
//
//
    }

    private fun getAllEvent(){
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val selection: String = "((${CalendarContract.Events.CALENDAR_ID} = ?))"
        val selectionArgs: Array<String> = arrayOf("${Constant.CALENDAR.CALENDAR_ID}")
        val cur: Cursor? = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
        // Use the cursor to step through the returned records
        cur?.let {
            while (it.moveToNext()) {
                // Get the field values
                val calID: Long = it.getLong(0)
                val title: String = it.getString(1)
                val dtstart: Long = it.getLong(3)
                // val ownerName: String = it.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
                println("data>>>>>> $calID $title -- ${Util.convertLongToTime(dtstart)}")
            }
        }

    }

    private fun callGetCalList(){
       viewModel.getCalList()
    }

    private  fun isEventExist(){
        viewModel.isEventExist(MeetingItem(name= "kriTest"))
    }
}

