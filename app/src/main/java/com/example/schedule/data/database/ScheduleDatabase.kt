package com.example.schedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.schedule.data.model.*

@Database(
    entities = [Platoon::class, Lesson::class, Note::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase: RoomDatabase()  {
        abstract fun scheduleDao(): ScheduleDao
}

