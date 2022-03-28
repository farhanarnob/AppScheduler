package com.farhanarnob.appscheduler.background_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.util.PKG_NAME
import com.farhanarnob.appscheduler.util.SCHEDULED_TIME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScheduleAppStartService(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        with(applicationContext as BaseAppApplication) {
            var result = Result.failure()
            val pkgName: String? = inputData.getString(PKG_NAME)
            val scheduledTime: Long = inputData.getLong(SCHEDULED_TIME,0L)
            if(scheduledTime != 0L && pkgName != null){
                withContext(Dispatchers.IO) {
                    val schedule =database.scheduleDao().getSchedule(scheduledTime)
                    if (schedule != null) {
                        result = Result.success()
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_LAUNCHER)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        intent.component = ComponentName(schedule.packageName, schedule.name)
                        startActivity(intent)
                        schedule.executed = true
                        database.scheduleDao().insertSchedule(schedule)
                    }
                }
            }
            return result
        }
    }
}