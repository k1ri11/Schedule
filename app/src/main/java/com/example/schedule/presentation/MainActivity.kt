package com.example.schedule.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.work.WorkInfo
import com.example.schedule.data.database.ScheduleDao
import com.example.schedule.data.workmanager.DownloadWorkManager.Companion.startWorker
import com.example.schedule.data.workmanager.WorkerKeys
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.schedule.R
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.presentation.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.schedule.domain.parser.ExelParser
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: ScheduleViewModel

    @Inject
    lateinit var dao: ScheduleDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }
    private fun createAndParse() {
        val parser = ExelParser(this)
        binding.startParseBtn.setOnClickListener {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    parser.parse()
        val tmpViewModel by viewModels<ScheduleViewModel>()
        viewModel = tmpViewModel
        binding.startParseBtn.setOnClickListener { startDownloading() }
        binding.checkPlatoonBtn.setOnClickListener {
            viewModel.platoonSchedule.observe(this) {
                Log.d("SCHEDULE", "$it")
            }
            viewModel.getPlatoonWithLessons(352, 8)
        }
    }

    fun startDownloading() {
        val workInfo = this.applicationContext.startWorker(3)
        workInfo.first.observe(this) { list ->
            val info = list.find { workInfo.second == it.id }
            when (info?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    val fileName = info.outputData.keyValueMap[WorkerKeys.FILE_URI] as String
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                        viewModel.parseExelAndUpdateDatabase(3)
                    }
                }
                WorkInfo.State.FAILED -> {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}

private fun BottomNavigationView.setupWithNavController(navController: NavController) {
    NavigationUI.setupWithNavController(this, navController)

}
