package com.example.schedule.data.model

sealed class LessonType{
    object Lecture: LessonType() // л - лекция
    object Practice: LessonType()  // пз - практическое занятие
    object Seminar: LessonType() // c - семинар
    object GroupExercise: LessonType() // гу - групповое занятие
    object SelfStudy: LessonType() // empty - самостоятельная работа
    object Test: LessonType() // зачет
    object Exam: LessonType() // экзамен
}

fun LessonType.lessonTypeToString(): String {
    return when (this) {
        LessonType.Lecture -> "лк"
        LessonType.Practice -> "пр"
        LessonType.Seminar -> "сем"
        LessonType.SelfStudy -> "ср"
        LessonType.GroupExercise -> "гз"
        LessonType.Test -> "зач"
        LessonType.Exam -> "экз"
    }
}

