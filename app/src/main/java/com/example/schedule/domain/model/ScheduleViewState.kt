package com.example.schedule.domain.model

data class ScheduleViewState(
    var platoonNumbers: List<Int> = emptyList(),
    var course: Int = 0,
    var platoon: Int = 0
)