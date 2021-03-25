package com.embedded.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.embedded.MyApp
import com.embedded.R
import com.embedded.model.MeetingItem
import com.embedded.ui.adapter.MeetingAdapter
import com.embedded.utils.Util
import kotlinx.android.synthetic.main.activity_dash_meeting.progress_bar
import kotlinx.android.synthetic.main.activity_meeting_list.*
import kotlinx.android.synthetic.main.content_meeting_list.*
import java.text.SimpleDateFormat
import java.util.*

class MeetingListActivity : BaseActivity() {

    private var meetingList = mutableListOf<MeetingItem>()
    private lateinit var meetingAdapter: MeetingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_list)
        setSupportActionBar(toolbar)
        initData()
        initView()

    }
    private fun initData() {
        meetingList =MyApp.instance.meetingList
    }
    private fun initView() {
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_action_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = getString(R.string.meeting_list)//"${rsLoginResponse?.data?.name}"
        meetingAdapter= MeetingAdapter(this,meetingList)
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MeetingListActivity)
            adapter = meetingAdapter
        }
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
}

