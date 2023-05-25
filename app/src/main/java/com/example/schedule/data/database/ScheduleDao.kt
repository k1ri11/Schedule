package com.example.schedule.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.schedule.data.model.*

@Dao
interface ScheduleDao {

    @Insert()
    suspend fun updatePlatoons(platoons :List<Platoon>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLessons(lessons :List<Lesson>)

    @Query("SELECT * FROM lessons WHERE platoon_number = :platoonNumber AND week_number = :weekNumber ORDER BY lesson_time")
    fun getPlatoonWithLessons(platoonNumber: Int, weekNumber: Int): List<Lesson>

    @Query("SELECT * FROM platoons")
    fun getPlatoons(): List<Platoon>

    @Query("DELETE FROM lessons")
    fun clearLessonsTable()

    @Query("DELETE FROM platoons")
    fun clearPlatoonsTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note : Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): List<Note>
}