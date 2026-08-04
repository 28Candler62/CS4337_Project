package com.example.projectcs4337.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "stops")
data class MetroStop(
    @PrimaryKey val id: Int,
    val StopId: String,
    val RouteId: String,
    val Name: String,
    val StopCode: String,
    val Type: String,
    val Lat: Double,
    val Lon: Double
)