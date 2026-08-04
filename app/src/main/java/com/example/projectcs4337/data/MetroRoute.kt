package com.example.projectcs4337.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "routes")
data class MetroRoute(
    @PrimaryKey val id: Int,
    val RouteId: String,
    val RouteName: String,
    val LongName: String,
    val RouteType: String
)