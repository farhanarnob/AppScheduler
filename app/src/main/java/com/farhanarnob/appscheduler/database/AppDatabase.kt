package com.hellodoc24.hellodoc24patientapp.data.source.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farhanarnob.appscheduler.model.Schedule
import com.hellodoc24.hellodoc24patientapp.data.source.database.dao.*


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(
    entities = [Schedule::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_0_1)
//                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        //
        private val MIGRATION_0_1: Migration = object : Migration(0, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                sample
//                database.execSQL("ALTER TABLE users "
//                        + " ADD COLUMN last_update INTEGER");
            }
        }
    }


}