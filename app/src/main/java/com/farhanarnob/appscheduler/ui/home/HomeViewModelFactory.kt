package com.farhanarnob.appscheduler.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farhanarnob.appscheduler.repository.ScheduleRepository
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase

class HomeViewModelFactory(
    private val database: AppDatabase,
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                database,scheduleRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}