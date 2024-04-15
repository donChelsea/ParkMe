package com.example.parkme.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parkme.domain.ParkingSpot
import com.example.parkme.domain.ParkingSpotRepository
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun MapScreen(
    viewModel: MapsViewModel = viewModel(),
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(MapEvent.ToggleAssassinsCreedMap)
            }) {
                Icon(
                    imageVector = if (viewModel.state.isAssassinsCreedMap) {
                        Icons.Default.ToggleOff
                    } else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Assassin's Creed map"
                )
            }
        }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            properties = viewModel.state.properties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            onMapLongClick = {
                viewModel.onEvent(MapEvent.OnMapLongClick(it))
            }
        ) {
            viewModel.state.parkingSpots.forEach { spot ->
                Marker(
                    state = MarkerState(position = LatLng(spot.lat, spot.lng)),
                    title = "Parking spot (${spot.lat}, ${spot.lng})",
                    snippet = "Long click to delete",
                    onInfoWindowLongClick = {
                        viewModel.onEvent(MapEvent.OnInfoWindowLongClick(spot))
                    },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
            }
        }
    }
}

@Immutable
data class MapState(
    val properties: MapProperties = MapProperties(),
    val isAssassinsCreedMap: Boolean = false,
    val parkingSpots: List<ParkingSpot> = emptyList()
)

sealed class MapEvent {
    data object ToggleAssassinsCreedMap: MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val spot: ParkingSpot): MapEvent()
}

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: ParkingSpotRepository
) : ViewModel() {
    var state by mutableStateOf(MapState())

    init {
        viewModelScope.launch {
            repository.getParkingSpots().collectLatest { spots ->
                state = state.copy(parkingSpots = spots)
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleAssassinsCreedMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isAssassinsCreedMap) {
                            null
                        } else MapStyleOptions(MapStyle.json)
                    ),
                    isAssassinsCreedMap = !state.isAssassinsCreedMap
                )
            }

            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    repository.deleteParkingSpot(event.spot)
                }
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    repository.insertParkingSpot(
                        ParkingSpot(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude
                        )
                    )
                }
            }
        }
    }
}