package com.farhanarnob.appscheduler.repository

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farhanarnob.appscheduler.model.asSchedule
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex

class ScheduleRepository private constructor(
    private val database: AppDatabase
) {
    private val mutex = Mutex()
    val _appList = MutableStateFlow<List<ResolveInfo>?>(null)
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
        _appList.emit(context.packageManager.queryIntentActivities(mainIntent, 0))
    }

    suspend fun checkInstalledApps(context: Context){
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(mainIntent, 0)
        saveInstalledAppsList(context,pkgAppsList)
    }

    suspend fun saveInstalledAppsList(context: Context,pkgAppList: List<ResolveInfo>){
        database.scheduleDao().insertSchedules(*pkgAppList.map {it.asSchedule(context)}.toTypedArray())
    }

    suspend fun clearSchedule() {
        database.scheduleDao().deleteAllSchedules()
    }
}