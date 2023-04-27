package com.example.schedule.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.databinding.FragmentSheduleBinding


class sheduleFragment : Fragment() {
    private lateinit var binding: FragmentSheduleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSheduleBinding.inflate(inflater, container, false)


        val weeks = mutableListOf<Week>()
        for (i in 1..16) {
            weeks.add(Week(i))

        }
        val lesson = mutableListOf<Shedule>()
        lesson.add(Shedule("9:00-10:30", "ОВП", "Крылов А.В.", "ауд. 301"))
        lesson.add(Shedule("10:40-12:10", "ОВП", "Крылов А.В.", "ауд. 301"))
        lesson.add(Shedule("13:00-14:30", "ОВП", "Крылов А.В.", "ауд. 301"))
        lesson.add(Shedule("14:40-16:10", "ВСП", "Гарманов С.С.", "ауд. 301"))
        // Настройка RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeeks.apply {
            setLayoutManager(layoutManager)
            adapter = WeekAdapter(weeks)
        }
        binding.recyclerViewShedule.apply {
            setLayoutManager(layoutManager1)
            adapter = SheduleAdapter(lesson)
        }
        return binding.root
    }

}

