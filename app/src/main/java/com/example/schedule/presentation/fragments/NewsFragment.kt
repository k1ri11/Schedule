package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.data.model.News
import com.example.schedule.databinding.FragmentNewsBinding
import com.example.schedule.domain.Resource
import com.example.schedule.presentation.adapters.NewsAdapter
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: ScheduleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        viewModel.getNews()
        setupNewsObserver()
        return binding.root
    }

    private fun setupNewsRecycler(data: List<News>) {
        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NewsAdapter(data)
        }
    }

    private fun setupNewsObserver() {
        viewModel.news.observe(viewLifecycleOwner){resource ->
            when(resource){
                is Resource.Success ->{
                    hideProgressBar()
                    setupNewsRecycler(resource.data!!)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "ошибка загрузки новостей", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> showProgressBar()
            }
        }
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }
}

