package com.farhanarnob.appscheduler.repository

import android.content.Context
import android.content.Intent
import com.farhanarnob.appscheduler.model.PackageInfo
import com.farhanarnob.appscheduler.model.Schedule
import com.farhanarnob.appscheduler.model.asPackage
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex

class ScheduleRepository private constructor(
    private val database: AppDatabase
) {
    private val mutex = Mutex()
    val _appList = MutableStateFlow<List<PackageInfo>?>(null)
    companion object {
        @Volatile
        private var INSTANCE: ScheduleRepository? = null
        fun getInstance(context: Context): ScheduleRepository {
            return INSTANCE ?: synchronized(ScheduleRepository::class.java) {
                return INSTANCE ?: ScheduleRepository(AppDatabase.getDatabase(context))
            }
        }
    }

    suspend fun loadInstalledApps(context: Context){
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkdList = context.packageManager.queryIntentActivities(mainIntent, 0)
        _appList.emit(pkdList.map {
            it.asPackage(context) }
        )
    }

//    suspend fun checkInstalledApps(context: Context){
//        val mainIntent = Intent(Intent.ACTION_MAIN, null)
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//        val pkgAppsList: List<ResolveInfo> =
//            context.packageManager.queryIntentActivities(mainIntent, 0)
//        saveInstalledAppsList(context,pkgAppsList)
//    }
//
//    suspend fun saveInstalledAppsList(context: Context,pkgAppList: List<ResolveInfo>){
//        database.scheduleDao().insertSchedules(*pkgAppList.map {it.asSchedule(context)}.toTypedArray())
//    }

    suspend fun saveAScheduledApp(name:String,
                                  appName: String, pkgApp: String,
                                  scheduleTime: Long): Long {
        Schedule(appName,pkgApp,name,false,scheduleTime)
        val existingSchedule = database.scheduleDao().getSchedule(scheduleTime)
        if(existingSchedule!= null){
            return 0L
        }
        return database.scheduleDao().insertSchedule(Schedule(appName = appName,pkgApp,name,
            false,scheduleTime))
    }
    suspend fun deleteASchedule(time: Long){
        database.scheduleDao().deleteSchedule(time)
    }
    suspend fun clearSchedule() {
        database.scheduleDao().deleteAllSchedules()
    }
}