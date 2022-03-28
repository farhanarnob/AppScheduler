package com.farhanarnob.appscheduler.model

import android.content.Context
import android.content.pm.ResolveInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    val appName:String,
    val packageName:String,
    val name:String,
    var executed: Boolean,
    @PrimaryKey
    val scheduledTime: Long,
)

fun ResolveInfo.asSchedule(context: Context):Schedule{
    return Schedule(
        appName = this.loadLabel(context.packageManager).toString(),
        packageName = this.activityInfo.packageName,
        name = this.activityInfo.name,
        scheduledTime = System.currentTimeMillis(),
        executed = false
    )
}
