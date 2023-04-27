package com.example.schedule.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.SheduleItemBinding

class SheduleAdapter(private val sheduler: List<Shedule>) :
    RecyclerView.Adapter<SheduleAdapter.SheduleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheduleViewHolder {
        val binding = SheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SheduleViewHolder, position: Int) {
        val currentItem = sheduler[position]
        holder.binding.sheduleItem.text = currentItem.Lesson
        holder.binding.timeItem.text = currentItem.Time
        holder.binding.auditoriumItem.text = currentItem.Auditorium
        holder.binding.teacherItem.text = currentItem.Teacher
    }

    override fun getItemCount(): Int {
        return sheduler.size
    }

    inner class SheduleViewHolder(val binding: SheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
