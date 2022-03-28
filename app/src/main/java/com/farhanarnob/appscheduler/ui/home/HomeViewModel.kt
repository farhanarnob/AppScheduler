package com.farhanarnob.appscheduler.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhanarnob.appscheduler.model.Schedule
import com.farhanarnob.appscheduler.repository.ScheduleRepository
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.*

class HomeViewModel(private val database: AppDatabase,
                    private val scheduleRepository: ScheduleRepository) : ViewModel() {
    private val appCoroutineScope = CoroutineScope(Dispatchers.IO)
    val appList = database.scheduleDao().getScheduleListFlow().asLiveData()

    fun startToCheckApp(context: Context) {
        appCoroutineScope.launch {
            scheduleRepository.clearSchedule()
        }
    }

    fun cleanScope(){
        appCoroutineScope.coroutineContext.cancelChildren()
    }

    fun deleteSchedule(schedule: Schedule) {
        appCoroutineScope.launch {
            database.scheduleDao().deleteSchedule(schedule)
        }
    }

}