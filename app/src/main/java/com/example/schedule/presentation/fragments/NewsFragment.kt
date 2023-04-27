package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.data.database.NewsDataSource
import com.example.schedule.data.model.News
import com.example.schedule.databinding.FragmentNewsBinding
import com.example.schedule.presentation.adapters.NewsAdapter
import com.example.schedule.presentation.adapters.WeekAdapter

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        val newsDataSet:List<News> = NewsDataSource().loadNews()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewNews.apply {
            setLayoutManager(layoutManager)
            adapter = NewsAdapter(newsDataSet)
        }
        return binding.root
    }
}

