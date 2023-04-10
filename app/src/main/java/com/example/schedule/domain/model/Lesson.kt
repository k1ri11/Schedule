package com.example.schedule.domain.model

data class Lesson(
    val name: String,
    val classRoom: String,
    val classRoomDesc: String,
    val teacher1: String,
    val teacher2: String,
) {
    override fun toString(): String {
        val str =
            "\n Lesson ( \n name = $name \n classRoom = $classRoom \n classRoomDesc = $classRoomDesc" +
                    " \n teacher1 = $teacher1 \n teacher2 = $teacher2)"
        return str
    }
}
