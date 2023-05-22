package com.example.schedule.data.model

sealed class LessonType{
    object Lecture: LessonType() // л - лекция
    object Practice: LessonType()  // пз - практическое занятие
    object Seminar: LessonType() // c - семинар
    object SelfStudy: LessonType() // empty - самостоятельная работа
    object Test: LessonType() // зачет
}

fun LessonType.lessonTypeToString(): String {
    return when (this) {
        LessonType.Lecture -> "лк"
        LessonType.Practice -> "пр"
        LessonType.Seminar -> "сем"
        LessonType.SelfStudy -> "ср"
        LessonType.Test -> "зач"
    }
}

