package com.example.schedule

import android.app.Application
import com.example.schedule.data.database.ScheduleDao
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application() {

    @Inject
    lateinit var scheduleDao : ScheduleDao

}