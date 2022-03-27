package com.farhanarnob.appscheduler.util

import android.content.Context
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.work.*
import com.farhanarnob.appscheduler.background_service.ScheduleAppStartService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

const val PKG_NAME = "PACKAGE_NAME"
object ScheduleServiceUtility {

    fun scheduleAppStartService(
        context: Context,specificTimeToTrigger :Long, pkgName: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val data: Data.Builder = Data.Builder()
            data.putString(PKG_NAME, pkgName)
            val currentTime = System.currentTimeMillis()
            val delayToPass = specificTimeToTrigger  - currentTime
            val workManager = WorkManager.getInstance(context)
            val constraints: Constraints = Constraints.Builder()
                .build()
            val scheduleLogOffService =
                OneTimeWorkRequestBuilder<ScheduleAppStartService>()
                    .setInitialDelay(delayToPass,TimeUnit.MILLISECONDS)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .setInputData(data.build())
                    .setConstraints(constraints).build()
            workManager.beginUniqueWork(
                specificTimeToTrigger .toString(),
                ExistingWorkPolicy.KEEP,
                scheduleLogOffService
            ).enqueue()
        }
    }
}