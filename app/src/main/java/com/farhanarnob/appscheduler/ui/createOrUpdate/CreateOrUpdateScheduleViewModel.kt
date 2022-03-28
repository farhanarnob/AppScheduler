package com.farhanarnob.appscheduler.ui.createOrUpdate

import android.content.Context
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.model.PackageInfo
import com.farhanarnob.appscheduler.repository.ScheduleRepository
import com.farhanarnob.appscheduler.util.ScheduleUtility
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateOrUpdateScheduleViewModel(private val database: AppDatabase,
                                      private val scheduleRepository: ScheduleRepository) : ViewModel() {
    private val appCoroutineScope = CoroutineScope(Dispatchers.IO)
    val success: LiveData<Any> get() = _success
    private val _success = MutableLiveData<Any>()
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

    fun saveASchedule(
        context: Context,
        packageInfo: PackageInfo?,
        scheduleTime: Long?,
        args: CreateOrUpdateScheduleFragmentArgs
    ) {
        appCoroutineScope.launch {
            if(packageInfo == null || scheduleTime == null){
                _success.postValue(R.string.select_time)
                return@launch
            }
            if(args.scheduleTime != 0L){
                scheduleRepository.deleteASchedule(args.scheduleTime)
            }
            val saved = scheduleRepository.saveAScheduledApp(appName = packageInfo.appName,
                name = packageInfo.name,
                pkgApp = packageInfo.packageName,
                scheduleTime = scheduleTime
            )
            if(saved == 0L){
                _success.postValue(false)
            }else{
                ScheduleUtility.scheduleAppStartService(context,scheduleTime,
                    packageInfo.packageName)
                _success.postValue(true)
            }
        }

    }

}