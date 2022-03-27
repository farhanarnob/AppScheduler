package com.farhanarnob.appscheduler.base

import android.app.Application
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase

class BaseAppApplication: Application() {
    val database by lazy {
        AppDatabase.getDatabase(this)
    }
}