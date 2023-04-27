package com.example.schedule.di

import android.content.Context
import androidx.room.Room
import com.example.schedule.data.database.ScheduleDao
import com.example.schedule.data.database.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideScheduleDatabase(@ApplicationContext context: Context): ScheduleDatabase {

        return Room.databaseBuilder(
            context.applicationContext,
            ScheduleDatabase::class.java,
            "schedule_database"
        ).build()
    }

    @Provides
    fun provideDao(db: ScheduleDatabase): ScheduleDao {
        return db.scheduleDao()
    }

}