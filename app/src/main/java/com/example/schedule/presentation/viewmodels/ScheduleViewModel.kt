package com.example.schedule.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.data.model.Lesson
import com.example.schedule.data.model.News
import com.example.schedule.data.repository.ScheduleRepository
import com.example.schedule.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    val news: LiveData<Resource<List<News>>> = repository.news
    val parseState: LiveData<Resource<String>> = repository.parseState
    val downloadingState: LiveData<Resource<String>> = repository.downloadingState
    val platoonSchedule: LiveData<Resource<List<Lesson>>> = repository.platoonSchedule

    fun parseExelAndUpdateDatabase(fileName: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.parseExelAndUpdateDatabase(fileName)
    }
    fun getPlatoonWithLessons(platoonNumber: Int, weekNumber: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlatoonWithLessons(platoonNumber, weekNumber)
        }
    fun getAllPlatoons() = repository.getPlatoons()

    fun getNews() = viewModelScope.launch(Dispatchers.IO) {
        repository.getNews()
    }
    fun getTeachers() = viewModelScope.launch(Dispatchers.IO) {
        repository.getTeachers()
    }

    fun downloadSchedule(course: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.downloadSchedule(course)
    }
}