package com.example.schedule.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lessons",
    indices = [Index(value = ["platoon_number", "lesson_name", "class_room", "main_teacher", "lesson_time", "week_number"], unique = true)]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long = 0,
    @ColumnInfo(name = "platoon_number")
    val platoonNumber: Int,
    @ColumnInfo(name = "lesson_name")
    val lessonName: String,
    @ColumnInfo(name = "class_room")
    val classRoom: String,
    @ColumnInfo(name = "lesson_type")
    val lessonType: LessonType,
    @ColumnInfo(name = "main_teacher")
    val mainTeacher: String,
    @ColumnInfo(name = "help_teacher")
    val helpTeacher: String,
    @ColumnInfo(name = "lesson_time")
    val lessonTime: String,
    @ColumnInfo(name = "week_number")
    val weekNumber: Int
)
