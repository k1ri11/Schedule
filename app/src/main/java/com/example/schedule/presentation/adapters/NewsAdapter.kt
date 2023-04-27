package com.example.schedule.presentation.adapters

import android.service.autofill.Dataset
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.schedule.R
import com.example.schedule.data.model.News
import com.example.schedule.databinding.NewsItemBinding
import com.example.schedule.databinding.WeekItemBinding


class NewsAdapter(private val dataset: List<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: NewsItemBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = dataset[position]
        holder.binding.newsItem.text = currentItem.newsText
        holder.binding.newsText.text = currentItem.newsId
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
