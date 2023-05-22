package com.example.schedule.di

import android.content.Context
import androidx.room.Room
import com.example.schedule.data.database.ScheduleDao
import com.example.schedule.data.database.ScheduleDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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

    @Provides
    fun provideFirebaseDatabase(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    fun provideFirebaseStorage(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }



}