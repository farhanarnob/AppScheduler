package com.farhanarnob.appscheduler.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id:Long
)
