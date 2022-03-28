package com.farhanarnob.appscheduler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farhanarnob.appscheduler.databinding.RvAppListItemBinding
import com.farhanarnob.appscheduler.model.Schedule
import com.farhanarnob.appscheduler.util.DateUtility

class ScheduleAdapter(val scheduleAdapterListener: ScheduleAdapterListener) :
    ListAdapter<Schedule, ScheduleAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(RvAppListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(
            oldItem: Schedule,
            newItem: Schedule,
        ): Boolean {
            if (oldItem.packageName != newItem.packageName) return false
            // check if id is the same
            return oldItem.packageName == newItem.packageName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Schedule,
            newItem: Schedule,
        ): Boolean {
            // check if content is the same
            // equals using data class
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: RvAppListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.tvName.text = schedule.appName
            binding.tvSubInfo.text = DateUtility.getTimeInString(
                DateUtility.WITH_SEC_DATE_FORMAT,
                schedule.scheduledTime
            )
            if(schedule.executed){
                binding.ivDelete.visibility = View.GONE
                binding.ivEdit.visibility = View.GONE
                binding.tvExecuted.visibility = View.VISIBLE
            }else{
                binding.ivDelete.visibility = View.VISIBLE
                binding.ivEdit.visibility = View.VISIBLE
                binding.tvExecuted.visibility = View.GONE
                binding.ivEdit.setOnClickListener {
                    scheduleAdapterListener.updateItemClick(schedule)
                }

                binding.ivDelete.setOnClickListener {
                    scheduleAdapterListener.deleteItemClick(schedule)
                }
            }
        }
    }

    interface ScheduleAdapterListener {
        fun updateItemClick(schedule: Schedule)
        fun deleteItemClick(schedule: Schedule)
    }
}
