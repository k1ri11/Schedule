package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.data.model.Teacher
import com.example.schedule.databinding.FragmentTeachersBinding
import com.example.schedule.domain.Resource
import com.example.schedule.presentation.adapters.TeachersAdapter
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeachersFragment : Fragment() {

    private lateinit var binding: FragmentTeachersBinding
    lateinit var viewModel: ScheduleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTeachersBinding.inflate(inflater, container, false)

        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        viewModel.getTeachers()
        setupTeachersObserver()
        return binding.root
    }

    private fun setupTeachersRecycler(data: List<Teacher>) {
        binding.recyclerViewTeachers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TeachersAdapter(data)
        }
    }
    private fun setupTeachersObserver() {
        viewModel.teachers.observe(viewLifecycleOwner){resource ->
            when(resource){
                is Resource.Success ->{
                    hideProgressBar()
                    setupTeachersRecycler(resource.data!!)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
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
