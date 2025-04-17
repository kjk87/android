package com.lejel.wowbox.apps.attendance.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.databinding.ItemAttendanceBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class AttendanceAdapter() : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    class ViewHolder(val binding: ItemAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val day = holder.absoluteAdapterPosition+1
        holder.binding.textAttendanceDay.text = holder.itemView.context.getString(R.string.format_days2, day)
        if(day < 7){
            holder.binding.textAttendancePoint.text = "+ ${day*5}"
        }else{
            holder.binding.textAttendancePoint.text = "+ 50"
        }

        if(LoginInfoManager.getInstance().isMember()){
            var attendanceCount = LoginInfoManager.getInstance().member!!.attendanceCount
            if(attendanceCount == null){
                attendanceCount = 0
            }
            if(position < attendanceCount){
                holder.binding.layoutAttendance.setBackgroundResource(R.drawable.bg_border_3px_ea5506_color_33ea5506_radius_10)
                holder.binding.imageAttendanceCheck.setImageResource(R.drawable.ic_attendance_check)
                holder.binding.textAttendancePoint.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
                holder.binding.textAttendanceDay.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
            }else{
                holder.binding.layoutAttendance.setBackgroundResource(R.drawable.bg_f7f7f7_radius_10)
                holder.binding.imageAttendanceCheck.setImageResource(R.drawable.ic_attendance_point)
                holder.binding.textAttendanceDay.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
                if(day < 7){
                    holder.binding.textAttendancePoint.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
                }else{
                    holder.binding.textAttendancePoint.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
                }
            }
        }else{
            holder.binding.layoutAttendance.setBackgroundResource(R.drawable.bg_f7f7f7_radius_10)
            holder.binding.imageAttendanceCheck.setImageResource(R.drawable.ic_attendance_point)
            holder.binding.textAttendanceDay.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
            if(day < 7){
                holder.binding.textAttendancePoint.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
            }else{
                holder.binding.textAttendancePoint.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}