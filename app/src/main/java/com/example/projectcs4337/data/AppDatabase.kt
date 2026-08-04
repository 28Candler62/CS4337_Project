package com.example.projectcs4337.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(
    entities = [
        MetroRoute::class,
        MetroStop::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RoutesDao(): RoutesDao
    abstract fun StopsDao(): StopsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "metro.db"
                )
                .createFromAsset("metro.db")
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}