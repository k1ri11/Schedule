package com.example.schedule.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "platoons",
    indices = [Index(value = ["platoon_number"], unique = true)]
)
data class Platoon(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "platoon_id")
    val platoonId: Long = 0,
    @ColumnInfo(name = "platoon_number")
    val platoonNumber: Int
)
