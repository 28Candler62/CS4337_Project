package com.example.projectcs4337.data

import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutesDao {
    @Query("SELECT * FROM routes")
     suspend fun getRoutes(): List<MetroRoute>
}