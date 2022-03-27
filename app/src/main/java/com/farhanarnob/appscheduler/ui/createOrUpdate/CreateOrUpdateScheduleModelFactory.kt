package com.farhanarnob.appscheduler.ui.createOrUpdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farhanarnob.appscheduler.repository.ScheduleRepository
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase

class CreateOrUpdateScheduleModelFactory(
    private val database: AppDatabase,
    private val scheduleRepository: ScheduleRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOrUpdateScheduleViewModel::class.java)) {
            return CreateOrUpdateScheduleViewModel(
                database,scheduleRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}