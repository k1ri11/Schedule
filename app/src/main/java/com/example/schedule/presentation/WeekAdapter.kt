package com.example.schedule.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.WeekItemBinding

class WeekAdapter(private val weeks: List<Week>) :
    RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val binding = WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val currentItem = weeks[position]
        holder.binding.weekDay.text = currentItem.weekNumber.toString()
    }

    override fun getItemCount(): Int {
        return weeks.size
    }

    inner class WeekViewHolder(val binding: WeekItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
