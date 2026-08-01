package com.example.projectcs4337.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "routes")
data class User(
    @PrimaryKey val id: Int,
    val RouteId: String,
    val namRouteNamee: String,
    val LongName: String,
    val RouteType: String
)