package com.example.projectcs4337

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectcs4337.data.MapMarker
import com.example.projectcs4337.data.MetroRoute
import com.example.projectcs4337.ui.theme.ProjectCS4337Theme
import com.example.projectcs4337.ui.viewModel.RouteSelectViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlin.time.Duration.Companion.milliseconds

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

@OptIn(ExperimentalMaterial3Api::class)
//@PreviewScreenSizes
@Composable
fun ProjectCS4337App() {
    val viewModel: RouteSelectViewModel = viewModel()
    val navController = rememberNavController()
    val allMarkers = viewModel.allMarkers.value
    val currentMarkers = remember { mutableStateListOf(*allMarkers.toTypedArray()) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routeStops by viewModel.getRouteStops(viewModel.selectedRoute.collectAsState().value).collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(currentRoute, routeStops, allMarkers) {
        currentMarkers.clear()
        val markersToDisplay = when (currentRoute) {
            "Destination1" -> allMarkers
            "Destination2" -> routeStops
            else -> emptyList()
        }

        currentMarkers.addAll(markersToDisplay)
    }

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
            ) {
                TopAppBar(
                    title = { Text("Where To From Here") }
                )

                MapComponent(
                    markers = currentMarkers,
                    modifier = Modifier.weight(1f)
                )

                NavHost(
                    navController = navController,
                    startDestination = "Destination1"
                ) {
                    composable("Destination1") { } //Destination1Screen() }
                    composable("Destination2") { } //Destination2Screen() }
                    composable("Destination3") { Destination3Screen(viewModel = viewModel) }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Destination3Screen(viewModel: RouteSelectViewModel) {
    val routes = viewModel.routes.value
    var expanded by remember { mutableStateOf(false) }
    val selectedRoute by viewModel.selectedRoute.collectAsStateWithLifecycle()
    var clickedRoute: MetroRoute? = routes.firstOrNull { it.RouteName == selectedRoute}

    Column(modifier = Modifier.fillMaxSize()) {
        Box {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .fillMaxWidth(),
                    readOnly = false,
                    value = selectedRoute,
                    onValueChange = { newText ->
//                        selectedRoute = newText
                        viewModel.selectRoute(newText)
                        // Optional: Automatically expand menu when typing
                        if (newText.isNotEmpty()) expanded = true
                    },
                    label = { Text("Select Route") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    routes.forEach { route ->
                        DropdownMenuItem(
                            text = { Text(text = route.RouteName) },
                            onClick = {
                                clickedRoute = route
                                viewModel.selectRoute(route.RouteName)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
        Column(Modifier.fillMaxSize()){
            Text(text = clickedRoute?.RouteName ?: "Select Valid Route")
            Text(text = clickedRoute?.LongName ?: "Select Valid Route")
            Text(text = clickedRoute?.RouteType ?: "Select Valid Route")
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun MapComponent(markers: List<MapMarker>, modifier: Modifier) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(29.766, -95.358), // Initial target coordinates
            16f                       // Initial zoom level
        )
    }
    var visibleBounds by remember { mutableStateOf<LatLngBounds?>(null) }
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .filter { !it } // Only proceed when NOT moving
            .debounce(300.milliseconds)  // Wait 300ms after movement stops
            .collect {
                visibleBounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
            }
    }
    val visibleMarkers = if (visibleBounds != null) {
        markers.filter { marker ->
            visibleBounds?.contains(LatLng(marker.lat, marker.lng)) == true
        }
    } else {
        emptyList()
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // 5. Render markers dynamically
        visibleMarkers.forEach { marker ->
            Marker(
                state = rememberUpdatedMarkerState(position = LatLng(marker.lat, marker.lng)),
                title = marker.title
            )
        }
    }

}