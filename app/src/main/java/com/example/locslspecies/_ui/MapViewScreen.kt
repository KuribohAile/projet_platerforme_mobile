package com.example.locslspecies._ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// fonction qui affiche la carte
@Composable
fun MapViewComposable() {
    val saguenay = LatLng(48.4149, -71.0489)
    val quebec = LatLng(46.829853, -71.254028)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(saguenay, 10f)
    }
    Column(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = saguenay),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
            Marker(
                state = MarkerState(position = quebec),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }


}