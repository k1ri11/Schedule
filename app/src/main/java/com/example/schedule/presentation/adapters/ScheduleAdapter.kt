package com.example.schedule.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.data.model.Lesson
import com.example.schedule.databinding.SheduleItemBinding

class ScheduleAdapter :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    var scheduler: List<Lesson> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = SheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val currentItem = scheduler[position]
        holder.binding.sheduleItem.text = currentItem.lessonName
        holder.binding.timeItem.text = currentItem.lessonTime
        holder.binding.auditoriumItem.text = currentItem.classRoom
        holder.binding.teacherItem.text = currentItem.mainTeacher
    }

    override fun getItemCount(): Int {
        return scheduler.size
    }

    inner class ScheduleViewHolder(val binding: SheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
