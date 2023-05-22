package com.example.schedule.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.WeekItemBinding

class WeekAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private val weeks = (1..16).toList()

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val binding = WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val currentItem = weeks[position]
        holder.binding.weekDay.text = currentItem.toString()
    }

    override fun getItemCount(): Int {
        return weeks.size
    }

    inner class WeekViewHolder(val binding: WeekItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            listener.onItemClick(adapterPosition)
        }
    }
}
