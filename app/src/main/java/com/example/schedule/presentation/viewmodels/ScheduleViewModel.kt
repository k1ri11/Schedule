package com.example.schedule.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.data.model.Lesson
import com.example.schedule.data.repository.ScheduleRepository
import com.example.schedule.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
): ViewModel() {

    val parseState: LiveData<Resource<String>> = repository.parseState

    val platoonSchedule: LiveData<Resource<List<Lesson>>> = repository.platoonSchedule

    fun parseExelAndUpdateDatabase(course: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.parseExelAndUpdateDatabase(course)
    }

    fun getPlatoonWithLessons(platoonNumber: Int, weekNumber: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.getPlatoonWithLessons(platoonNumber, weekNumber)
    }
}