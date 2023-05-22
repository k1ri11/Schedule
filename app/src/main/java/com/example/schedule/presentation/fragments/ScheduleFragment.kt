package com.example.schedule.presentation.fragments

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.schedule.R
import com.example.schedule.data.workmanager.DownloadWorkManager.Companion.startWorker
import com.example.schedule.data.workmanager.WorkerKeys
import com.example.schedule.databinding.DialogSelectPlatoonBinding
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.schedule.domain.Resource
import com.example.schedule.domain.model.ScheduleViewState
import com.example.schedule.presentation.adapters.ScheduleAdapter
import com.example.schedule.presentation.adapters.WeekAdapter
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ScheduleFragment : Fragment(), WeekAdapter.OnItemClickListener {
    private lateinit var binding: FragmentScheduleBinding

    lateinit var viewModel: ScheduleViewModel
    private val scheduleAdapter = ScheduleAdapter()
    private lateinit var dialogBinding: DialogSelectPlatoonBinding
    private lateinit var prefs: SharedPreferences

    private val viewState = ScheduleViewState()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        prefs = requireContext().getSharedPreferences("storage", Context.MODE_PRIVATE)
        setupSettingsButtonListener()
        setupDownloadingStateObserver()
        checkPreviousUIState()
        setupRecyclers()
        if (viewState.platoon == 0 || viewState.course == 0) setupDialog()
        else {
            viewModel.getPlatoonWithLessons(viewState.platoon, 2)
        }
        setupParseStateObserver()
        setupPlatoonScheduleObserver()
        return binding.root
    }

    private fun setupSettingsButtonListener() {
        binding.settingsButton.setOnClickListener {
            setupDialog()
        }
    }

    private fun checkPreviousUIState() {
        val course = prefs.getInt("course", 0)
        val platoon = prefs.getInt("platoon", 0)
        viewState.course = course
        viewState.platoon = platoon
    }

    private fun setupDialog() {
        val dialog = Dialog(
            requireContext(),
            com.google.android.material.R.style.ThemeOverlay_Material3_DayNight_BottomSheetDialog
        )
        dialog.setCancelable(false)
        dialogBinding = DialogSelectPlatoonBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        setupCourseSpinner()
        dialogBinding.saveButton.setOnClickListener {
            if (dialogBinding.platoonSpinner.adapter == null) {
                val newCourse = dialogBinding.courseSpinner.selectedItemPosition + 3
                if (newCourse == viewState.course) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val platoons = viewModel.getAllPlatoons()
                        viewState.platoonNumbers = platoons.map { it.platoonNumber }
                    }
                    showPlatoonSection()
                    setupPlatoonSpinner(viewState.platoonNumbers)
                } else {
                    viewState.course = dialogBinding.courseSpinner.selectedItemPosition + 3
                    viewModel.downloadSchedule(viewState.course)

                    showPlatoonSection()
                }
            } else {
                setupRecyclers()
                val course = dialogBinding.courseSpinner.selectedItemPosition + 3
                val platoonIndex = dialogBinding.platoonSpinner.selectedItemPosition
                val platoonNumber = viewState.platoonNumbers[platoonIndex]
                viewState.course = course
                viewState.platoon = platoonNumber
                updateUIStatePrefs()
                viewModel.getPlatoonWithLessons(platoonNumber, 2)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun updateUIStatePrefs() {
        prefs.edit {
            putInt("course", viewState.course)
            putInt("platoon", viewState.platoon)
        }
    }

    private fun showPlatoonSection() {
        dialogBinding.platoonSpinner.visibility = View.VISIBLE
        dialogBinding.platoonText.visibility = View.VISIBLE
    }


    private fun setupCourseSpinner() {
        val courseSpinner = dialogBinding.courseSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            courseSpinner.adapter = adapter
        }
    }

    private fun setupParseStateObserver() {
        viewModel.parseState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Successfully parsed", Toast.LENGTH_SHORT)
                        .show()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val platoons = viewModel.getAllPlatoons()
                        viewState.platoonNumbers = platoons.map { it.platoonNumber }
                        withContext(Dispatchers.Main) { setupPlatoonSpinner(viewState.platoonNumbers) }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun setupPlatoonScheduleObserver() {
        viewModel.platoonSchedule.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    val schedule = result.data!!
                    scheduleAdapter.scheduler = schedule
                    if (schedule.isEmpty()) {
                        Toast.makeText(requireContext(), "Выходной", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun setupRecyclers() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeeks.apply {
            setLayoutManager(layoutManager)
            adapter = WeekAdapter(this@ScheduleFragment)
        }
        binding.recyclerViewShedule.apply {
            setLayoutManager(layoutManager1)
            adapter = scheduleAdapter
        }
    }

    private fun setupPlatoonSpinner(platoonNumbers: List<Int>) {
        val platoonSpinner = dialogBinding.platoonSpinner
        val platoonAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            platoonNumbers
        )
        platoonSpinner.adapter = platoonAdapter
    }

    private fun setupDownloadingStateObserver() {
        viewModel.downloadingState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Successfully downloaded", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.parseExelAndUpdateDatabase(result.data!!)
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {}
            }
        }
    }

    private fun startDownloadingAndUpdatingDatabase() {
        val workInfo = requireContext().applicationContext.startWorker(viewState.course)
        workInfo.first.observe(viewLifecycleOwner) { list ->
            val info = list.find { workInfo.second == it.id }
            when (info?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(requireContext(), "downloading successful", Toast.LENGTH_SHORT)
                        .show()
                    val fileName = info.outputData.keyValueMap[WorkerKeys.FILE_URI] as String
                    viewModel.parseExelAndUpdateDatabase(fileName)
                }

                WorkInfo.State.FAILED -> {
                    Toast.makeText(requireContext(), "downloading failed", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }

    override fun onItemClick(position: Int) {
        viewModel.getPlatoonWithLessons(viewState.platoon, position + 1)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}

