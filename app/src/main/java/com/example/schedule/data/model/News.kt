package com.example.schedule.data.model

import androidx.annotation.DrawableRes

data class News(
    val newsText: String,
    val newsId: String,
    @DrawableRes val newsPhoto: Int
)

