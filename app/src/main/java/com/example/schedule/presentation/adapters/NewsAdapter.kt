package com.example.schedule.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schedule.data.model.News
import com.example.schedule.databinding.NewsItemBinding


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
        holder.binding.apply {
            newsTitle.text = currentItem.title
            newsText.text = currentItem.text
            Glide.with(this.root).load(currentItem.images.first()).into(newsImage)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
