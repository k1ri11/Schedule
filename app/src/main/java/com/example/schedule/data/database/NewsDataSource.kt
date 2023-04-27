package com.example.schedule.data.database
import android.annotation.SuppressLint
import com.example.schedule.R
import com.example.schedule.data.model.News
class NewsDataSource {
    @SuppressLint("ResourceType")
    fun loadNews():List<News>{
        return listOf(
            News("Ужасы на военке","Происходили настоящие ужасы",R.drawable.b86c63024eeb6a82a7f44a2546cd6f71),
            News("Ужасы на военке","Происходили настоящие ужасы",R.drawable.b86c63024eeb6a82a7f44a2546cd6f71),
            News("Ужасы на военке","Происходили настоящие ужасы",R.drawable.b86c63024eeb6a82a7f44a2546cd6f71)

        )

    }
}