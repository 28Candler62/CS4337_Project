package com.example.projectcs4337.data

import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

data class MapMarker(
    val lat: Double,
    val lng: Double,
    val title: String
)
@Dao
interface StopsDao {
    @Query("SELECT * FROM stops")
    suspend fun getStops(): List<MetroStop>

    @Query("SELECT Lat as lat, Lon as lng, RouteId as title FROM stops")
    suspend fun getAllMarkers(): List<MapMarker>

    @Query("SELECT Lat as lat, Lon as lng, Name as title FROM stops WHERE RouteId = :route")
    fun getRouteStops(route: String): Flow<List<MapMarker>>
}