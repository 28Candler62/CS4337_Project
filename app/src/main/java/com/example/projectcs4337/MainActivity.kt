package com.example.projectcs4337

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectcs4337.ui.theme.ProjectCS4337Theme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectCS4337Theme {
                ProjectCS4337App()
            }
        }
    }
}

data class MapMarker(val lat: Double, val lng: Double, val title: String)

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun ProjectCS4337App() {
    val navController = rememberNavController()
    val currentMarkers = remember { mutableStateListOf<MapMarker>() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(
//            LatLng(32.77, -96.79), // Initial target coordinates
//            10f                       // Initial zoom level
//        )
//    }

    LaunchedEffect(currentRoute) {
        currentMarkers.clear()
        currentMarkers.addAll(getMarkersForRoute(currentRoute))
    }

    // Get system insets manually
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            // Items...
            item(selected = currentRoute == "Destination1", icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = "HOME") }, onClick = { navController.navigate("Destination1") }) //{ Text("Dest 1") }
            item(selected = currentRoute == "Destination2", icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = "HOME") }, onClick = { navController.navigate("Destination2") }) //{ Text("Dest 2") }
            item(selected = currentRoute == "Destination3", icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = "HOME") }, onClick = { navController.navigate("Destination3") }) //{ Text("Dest 3") }
        },
        content = { // No innerPadding parameter here
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // Manually apply padding for Status Bar and Bottom Nav height
//                    .padding(
//                        top = systemBarsPadding.calculateTopPadding(),
//                        bottom = systemBarsPadding.calculateBottomPadding()
//                    )
            ) {
                TopAppBar(title = { Text("My Map App") })

                MapComponent(
                    markers = currentMarkers,
                    modifier = Modifier.weight(1f)
                )

                NavHost(
                    navController = navController,
                    startDestination = "Destination1"
                ) {
                    composable("Destination1") { Destination1Screen() }
                    composable("Destination2") { Destination2Screen() }
                    composable("Destination3") { Destination3Screen() }
                }
            }
        }
    )
}

fun getMarkersForRoute(route: String?): List<MapMarker> {
    return when (route) {
        "Destination1" -> d1Markers()
        "Destination2" -> listOf(
            MapMarker(34.0522, -118.2437, "LA Store")
        )
        "Destination3" -> listOf(
            MapMarker(40.7128, -74.0060, "NY Hub"),
            MapMarker(40.7228, -74.0160, "NY Depot")
        )
        else -> emptyList()
    }
}

fun d1Markers(): List<MapMarker>{
    return listOf(
//        MapMarker(37.7749, -122.4194, "SF Office"),
        MapMarker(29.766199, -95.359270, "UHD?"),
        MapMarker(37.7849, -122.4094, "SF Warehouse")
    )
}

@Composable
fun Destination1Screen() {
    //    Text("This is the Home Screen")
}
@Composable
fun Destination2Screen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("This is the Home Screen")
    }
    // Add markers specific to Destination 1
}
@Composable
fun Destination3Screen() {
//    Text("This is the Home Screen")
    // Add markers specific to Destination 1
}

@Composable
fun MapComponent(markers: List<MapMarker>, modifier: Modifier) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(29.766, -95.358), // Initial target coordinates
            14f                       // Initial zoom level
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // 5. Render markers dynamically
        markers.forEach { marker ->
            Marker(
                state = rememberUpdatedMarkerState(position = LatLng(marker.lat, marker.lng)),
                title = marker.title
            )
        }
    }

}