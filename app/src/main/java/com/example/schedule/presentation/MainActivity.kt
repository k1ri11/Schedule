package com.example.schedule.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
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
        val parser = ExelParser(this)
        binding.startParseBtn.setOnClickListener {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    parser.parse()
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity, "parse took $time ms", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}