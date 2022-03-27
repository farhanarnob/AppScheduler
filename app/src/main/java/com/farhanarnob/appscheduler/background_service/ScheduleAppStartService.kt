package com.farhanarnob.appscheduler.background_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.util.PKG_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScheduleAppStartService(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        with(applicationContext as BaseAppApplication) {
            var result = Result.failure()
            val pkgName: String? = inputData.getString(PKG_NAME)
            withContext(Dispatchers.IO) {
                pkgName?.let { pkgName ->
                    result = Result.success()
                    val schedule =database.scheduleDao().getSchedule(pkgName)
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    intent.component = schedule?.packageName?.let {
                        ComponentName(it, schedule.name) }
                    startActivity(intent)
                }
            }
            return result
        }
    }
}