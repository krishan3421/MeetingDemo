package com.embedded.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.embedded.R
import com.embedded.model.MeetingItem
import java.text.SimpleDateFormat
import java.util.*


class MeetingAdapter(val mContext: Context, var list:MutableList<MeetingItem>) : RecyclerView.Adapter<MeetingAdapter.OrderViewHolder>() {

    lateinit var meetingClickListener: MeetingClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.meeting_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        list[position].let {
            holder.apply {
                productName.text = "Product: ${it.product}"
                name.text="Name: ${it.name}"
                email.text="Email: ${it.email}"
                var startTime = "Start Time:"
                if(!it.startDate.isNullOrEmpty()){
                    startTime = "$startTime ${it.startDate}"
                }
                if(!it.startTime.isNullOrEmpty()){
                    startTime = "$startTime ${it.startTime}"
                }
                startDateTimeText.text= startTime
                if(it.phone.isNullOrEmpty()){
                    mobile.visibility=View.GONE
                }else{
                    mobile.text="Mobile: ${it.phone}"
                    mobile.visibility=View.VISIBLE
                }
                meetingId.text="MeetingId: ${it.meetingid}"
//                var createComDate=  ""
//                if(!it.startDate.isNullOrEmpty()){
//                    createComDate=it.startDate
//                }
//                if(!it.startTime.isNullOrEmpty()){
//                    createComDate="$createComDate ${it.startTime}"
//                }
//                println("createComDate>>>>>>> $createComDate")
//               // var temp = "2021-02-18 13:30:00"
//                val itemDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createComDate)
//                val differenceInTime: Long = Date().time - itemDate.time
//                val differenceInHours: Long = differenceInTime / (60 * 60 * 1000);
//                println("difference>>>>>>> $differenceInHours")
//                if(Date() == itemDate ||(Date().after(itemDate) && differenceInHours <=2)){
//                    joinMeetingButton.visibility=View.VISIBLE
//                }else{
//                    joinMeetingButton.visibility=View.GONE
//                }
            }
        }
        holder.joinMeetingButton.setOnClickListener {
            meetingClickListener.onItemClick(list[position], it)
        }


    }

    fun setOnItemClickListener(meetingClickListener: MeetingClickListener) {
        this.meetingClickListener = meetingClickListener
    }
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val productName by lazy { itemView.findViewById(R.id.product_text) as TextView }
        val name by lazy { itemView.findViewById(R.id.name_text) as TextView }
        val email by lazy { itemView.findViewById(R.id.email_text) as TextView }
        val mobile by lazy { itemView.findViewById(R.id.phone_text) as TextView }
        val meetingId by lazy { itemView.findViewById(R.id.meeting_id) as TextView }
        val joinMeetingButton by lazy { itemView.findViewById(R.id.join_meeting_button) as Button }
        val startDateTimeText by lazy { itemView.findViewById(R.id.start_date_time_text) as TextView }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            //meetingClickListener.onItemClick(list[adapterPosition], v)
        }
    }

    public fun addAll(mlist: MutableList<MeetingItem>){
        list.clear()
        list.addAll(mlist)
        notifyDataSetChanged()
    }

    interface MeetingClickListener {
        fun onItemClick(meetingItem: MeetingItem, v: View)
    }
}