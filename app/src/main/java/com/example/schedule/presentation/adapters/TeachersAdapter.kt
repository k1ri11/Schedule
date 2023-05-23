package com.example.schedule.presentation.adapters

import android.service.autofill.Dataset
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schedule.data.model.Teacher
import com.example.schedule.databinding.TeachersItemBinding

class TeachersAdapter(private val dataset: List<Teacher>):
    RecyclerView.Adapter<TeachersAdapter.TeachersViewHolder>() {


    inner class TeachersViewHolder(val binding: TeachersItemBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeachersAdapter.TeachersViewHolder {
        val binding = TeachersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeachersViewHolder(binding)

    }
    override fun onBindViewHolder(holder: TeachersAdapter.TeachersViewHolder, position: Int) {
        val currentItem = dataset[position]
        holder.binding.apply {
            teachersTitle.text = currentItem.name
            teachersText.text = currentItem.post
            teachersMail.text = currentItem.email
            teachersAuditorium.text = currentItem.classRoom
            teachersNumber.text = currentItem.telephone
            Glide.with(this.root).load(currentItem.image).into(teacherItem)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
    }