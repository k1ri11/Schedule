package com.example.schedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.schedule.data.database.ScheduleDao
import com.example.schedule.data.model.Lesson
import com.example.schedule.data.model.Platoon
import com.example.schedule.domain.Resource
import com.example.schedule.domain.parser.ExelParser
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val exelParser: ExelParser,
    @ApplicationContext private val context: Context,
    private val dao: ScheduleDao,
) {

    private val _parseState = MutableLiveData<Resource<String>>(Resource.Loading())
    val parseState: LiveData<Resource<String>> = _parseState

    private val _platoonSchedule = MutableLiveData<Resource<List<Lesson>>>(Resource.Loading())
    val platoonSchedule: LiveData<Resource<List<Lesson>>> = _platoonSchedule

    suspend fun parseExelAndUpdateDatabase(course: Int) {
        val file = when (course) {
            3 -> File(context.filesDir, "shedule_vuc.xlsx")
            4 -> File(context.filesDir, "shedule_vuc.xlsx")
            5 -> File(context.filesDir, "shedule_vuc.xlsx")
            else -> null
        }
        file?.let { exelParser.parse(it) }
            ?: _parseState.postValue(Resource.Error("can't get file"))
        val parsedData = exelParser.getParsedData()
        if (parsedData is Resource.Success) {
            val platoons = parsedData.data!!.keys.map { Platoon(platoonNumber = it) }
            dao.clearPlatoonsTable()
            dao.updatePlatoons(platoons)
            val lessonsList = mutableListOf<Lesson>()
            parsedData.data.forEach { (_, lists) ->
                for (list in lists) {
                    lessonsList.addAll(list)
                }
            }
            dao.clearLessonsTable()
            dao.updateLessons(lessonsList)
            _parseState.postValue(Resource.Success(""))
        } else {
            _parseState.postValue(Resource.Error("${parsedData.message}"))
        }
    }

    fun getPlatoonWithLessons(platoonNumber: Int, weekNumber: Int) {
        // todo coroutineExceptionHandlers
        val result = dao.getPlatoonWithLessons(platoonNumber, weekNumber)
        _platoonSchedule.postValue(Resource.Success(result))
    }

}