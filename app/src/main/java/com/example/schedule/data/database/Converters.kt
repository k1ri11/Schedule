package com.example.schedule.data.database

import androidx.room.TypeConverter
import com.example.schedule.data.model.LessonType

class Converters {
    @TypeConverter
    fun subjectTypeToString(importance: LessonType): String {
        return when (importance) {
            LessonType.Lecture -> "lecture"
            LessonType.Practice -> "practice"
            LessonType.Seminar -> "seminar"
            LessonType.SelfStudy -> "selfStudy"
            LessonType.Test -> "test"
        }
    }

    @TypeConverter
    fun stringToSubjectType(strImportance: String): LessonType {
        return when (strImportance) {
            "lecture" -> LessonType.Lecture
            "practice" -> LessonType.Practice
            "seminar" -> LessonType.Seminar
            "selfStudy" -> LessonType.SelfStudy
            "test" -> LessonType.Test
            else -> LessonType.Lecture
        }
    }
}