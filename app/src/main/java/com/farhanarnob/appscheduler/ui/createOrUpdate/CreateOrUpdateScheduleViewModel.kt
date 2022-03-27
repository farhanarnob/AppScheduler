package com.farhanarnob.appscheduler.ui.createOrUpdate

import android.content.Context
import android.content.pm.ResolveInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhanarnob.appscheduler.repository.ScheduleRepository
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateOrUpdateScheduleViewModel(private val database: AppDatabase,
                                      private val scheduleRepository: ScheduleRepository) : ViewModel() {
    private val appCoroutineScope = CoroutineScope(Dispatchers.IO)
    val appList = scheduleRepository._appList.asLiveData()
//    fun startToCheckApp(context: Context) {
//        appCoroutineScope.launch {
//            scheduleRepository.checkInstalledApps(context)
//        }
//    }

    fun loadInstalledAppList(context: Context){
        appCoroutineScope.launch {
            scheduleRepository.loadInstalledApps(context)
        }
    }

    fun cleanScope(){
        appCoroutineScope.coroutineContext.cancelChildren()
    }

}