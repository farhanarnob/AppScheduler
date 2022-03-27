package com.hellodoc24.hellodoc24patientapp.data.source.database.dao

import androidx.room.*
import com.farhanarnob.appscheduler.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedules(vararg schedule: Schedule)

    @Query("SELECT * FROM Schedule ORDER BY appName")
    fun getScheduleListFlow(): Flow<List<Schedule>?>

    @Query("SELECT * FROM Schedule where packageName = :pkgName ORDER BY appName limit 1")
    suspend fun getSchedule(pkgName: String): Schedule?

    @Query("DELETE FROM Schedule")
    suspend fun deleteAllSchedules()

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
}