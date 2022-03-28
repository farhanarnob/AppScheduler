package com.hellodoc24.hellodoc24patientapp.data.source.database.dao

import androidx.room.*
import com.farhanarnob.appscheduler.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(vararg schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule):Long

    @Query("SELECT * FROM Schedule ORDER BY appName")
    fun getScheduleListFlow(): Flow<List<Schedule>?>

    @Query("SELECT * FROM Schedule where packageName = :pkgName ORDER BY appName limit 1")
    suspend fun getSchedule(pkgName: String): Schedule?


    @Query("SELECT * FROM Schedule where scheduledTime = :time ORDER BY appName limit 1")
    suspend fun getSchedule(time: Long): Schedule?

    @Query("DELETE FROM Schedule")
    suspend fun deleteAllSchedules()

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
}