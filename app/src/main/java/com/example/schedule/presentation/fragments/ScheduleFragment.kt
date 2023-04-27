package com.example.schedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.schedule.data.workmanager.DownloadWorkManager.Companion.startWorker
import com.example.schedule.data.workmanager.WorkerKeys
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.schedule.domain.Resource
import com.example.schedule.presentation.Week
import com.example.schedule.presentation.adapters.ScheduleAdapter
import com.example.schedule.presentation.adapters.WeekAdapter
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding

    lateinit var viewModel: ScheduleViewModel
    private val scheduleAdapter = ScheduleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        val weeks = mutableListOf<Week>()
        for (i in 1..16) {
            weeks.add(Week(i))

        }
        // Настройка RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeeks.apply {
            setLayoutManager(layoutManager)
            adapter = WeekAdapter(weeks)
        }
        binding.recyclerViewShedule.apply {
            setLayoutManager(layoutManager1)
            adapter = scheduleAdapter
        }

        startDownloadingAndUpdatingDatabase()
        viewModel.platoonSchedule.observe(viewLifecycleOwner){result ->
            when(result){
                is Resource.Success -> {
                    scheduleAdapter.scheduler = result.data!!
                    //todo притащить diffutil
                    scheduleAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }

        return binding.root
    }

    private fun startDownloadingAndUpdatingDatabase() {
        val workInfo = requireContext().applicationContext.startWorker(3)
        workInfo.first.observe(viewLifecycleOwner) { list ->
            val info = list.find { workInfo.second == it.id }
            when (info?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    val fileName =
                        info.outputData.keyValueMap[WorkerKeys.FILE_URI] as String
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                        viewModel.parseExelAndUpdateDatabase(3)
                        viewModel.getPlatoonWithLessons(352, 9)
                    }
                }
                WorkInfo.State.FAILED -> {
                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

}

