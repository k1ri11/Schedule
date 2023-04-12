package com.example.schedule.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.work.WorkInfo
import com.example.schedule.data.workmanager.DownloadWorkManager.Companion.startWorker
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.domain.parser.ExelParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workInfo = this.applicationContext.startWorker(3)
        workInfo.first.observe(this) { list ->
            val info = list.find { workInfo.second == it.id }
            when (info?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()}
                WorkInfo.State.FAILED -> {Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()}
                else -> {}
            }
        }
        createAndParse()
    }

    private fun createAndParse() {
        val parser = ExelParser(this)
        binding.startParseBtn.setOnClickListener {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    parser.parse()
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "parse took $time ms", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }
}