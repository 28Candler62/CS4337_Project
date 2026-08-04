package com.example.projectcs4337.ui.viewModel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectcs4337.data.AppDatabase
import com.example.projectcs4337.data.MapMarker
import com.example.projectcs4337.data.MetroRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//class RouteSelectViewModel(private val routesDao: RoutesDao) : ViewModel() {
class RouteSelectViewModel(application: Application) : AndroidViewModel(application) {
    // Initialize DAOs internally
    private val database = AppDatabase.getDatabase(application)
    private val routesDao = database.RoutesDao()
    private val stopsDao = database.StopsDao()
    val routes: MutableState<List<MetroRoute>> = mutableStateOf(emptyList())
    val allMarkers: MutableState<List<MapMarker>> = mutableStateOf(emptyList())

    // Hoisted state for selection
//    private val _selectedRoute = MutableStateFlow<MetroRoute?>(null)
    private val _selectedRoute = MutableStateFlow("700")
    val selectedRoute: StateFlow<String> = _selectedRoute.asStateFlow()


//    val RouteStops: Flow<List<MapMarker>> = stopsDao.getRouteStops(route)

    fun selectRoute(route: String) {
        _selectedRoute.value = route
    }
    fun getRouteStops(route: String): Flow<List<MapMarker>> {
        val r:String = "Ho414_4620_$route"
        val routeStops: Flow<List<MapMarker>> = (stopsDao.getRouteStops(r))
        return routeStops
    }


    init {
        viewModelScope.launch {
            routes.value = routesDao.getRoutes()
            allMarkers.value = stopsDao.getAllMarkers()
            _selectedRoute.value = "700"
//            if (routes != emptyList<MetroRoute>()) {
//                _selectedRoute.value = routes.value.first().RouteId
//            } else {
//                _selectedRoute.value = "700"
//            }
        }
    }
}