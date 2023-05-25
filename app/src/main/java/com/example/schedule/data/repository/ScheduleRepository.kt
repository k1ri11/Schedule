package com.example.schedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import com.example.schedule.data.database.ScheduleDao
import com.example.schedule.data.model.Lesson
import com.example.schedule.data.model.News
import com.example.schedule.data.model.Note
import com.example.schedule.data.model.Platoon
import com.example.schedule.data.model.Teacher
import com.example.schedule.domain.Resource
import com.example.schedule.domain.parser.ExelParser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val exelParser: ExelParser,
    @ApplicationContext private val context: Context,
    private val dao: ScheduleDao,
    private val cloudStore: FirebaseFirestore,
    private val fireStore: StorageReference
) {

    private val _downloadingState = MutableLiveData<Resource<String>>(Resource.Loading())
    val downloadingState: LiveData<Resource<String>> = _downloadingState

    private val _getNotes = MutableLiveData<List<Note>>()
    val getNotes: LiveData<List<Note>> = _getNotes

    private val _parseState = MutableLiveData<Resource<String>>(Resource.Loading())
    val parseState: LiveData<Resource<String>> = _parseState

    private val _platoonSchedule = MutableLiveData<Resource<List<Lesson>>>(Resource.Loading())
    val platoonSchedule: LiveData<Resource<List<Lesson>>> = _platoonSchedule

    private val _news = MutableLiveData<Resource<List<News>>>(Resource.Loading())
    val news: LiveData<Resource<List<News>>> = _news

    private val _teachers = MutableLiveData<Resource<List<Teacher>>>(Resource.Loading())
    val teachers: LiveData<Resource<List<Teacher>>> = _teachers


    suspend fun downloadSchedule(course: Int) {
        _downloadingState.postValue(Resource.Loading())
        val file = File(context.filesDir, "downloaded_course_$course.xlsx")
        val task = fireStore.child("shedule_vuc_$course.xlsx").getFile(file)
        task.addOnCompleteListener {
            _downloadingState.postValue(Resource.Success(file.name))
        }
        task.addOnFailureListener { exception ->
            _downloadingState.postValue(Resource.Error(exception.message ?: "download Failed"))
        }
    }


    suspend fun parseExelAndUpdateDatabase(fileName: String) {
        val file = File(context.filesDir, fileName)
        exelParser.parse(file)
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
        val result = dao.getPlatoonWithLessons(platoonNumber, weekNumber)
        _platoonSchedule.postValue(Resource.Success(result))
    }

    fun getNews() {
        _news.postValue(Resource.Loading())
        val news = mutableListOf<News>()
        cloudStore.collection("news").get().addOnSuccessListener {
            it.forEach { snap ->
                val item = snap.toObject(News::class.java)
                news.add(item)
            }
            _news.postValue(Resource.Success(news))
        }.addOnFailureListener {
            _news.postValue(Resource.Error("${it.message}"))
        }
    }

    fun getTeachers() {
        _teachers.postValue(Resource.Loading())
        val teachers = mutableListOf<Teacher>()
        cloudStore.collection("teachers").get().addOnSuccessListener {
            it.forEach { snap ->
                val item = snap.toObject(Teacher::class.java)
                teachers.add(item)
            }
            _teachers.postValue(Resource.Success(teachers))
        }.addOnFailureListener {
            _teachers.postValue(Resource.Error("${it.message}"))
        }
    }

    fun getPlatoons() = dao.getPlatoons()

    suspend fun getNotes() {
        _getNotes.postValue(dao.getAllNotes())
    }

    suspend fun insert(note: Note) {
        dao.insert(note)
    }

    suspend fun delete(note: Note) {
        dao.delete(note)
    }

    suspend fun update(note: Note) {
        dao.update(note)
    }

}