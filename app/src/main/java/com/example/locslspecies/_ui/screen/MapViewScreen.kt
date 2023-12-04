package com.example.locslspecies._ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies.viewmodel.AuthViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// fonction qui affiche la carte
@Composable
fun MapViewScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = viewModel()
    val pictures by viewModel.pictures.observeAsState(emptyList())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.4149, -71.0489), 10f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            pictures.forEachIndexed { index, picture ->

                if (picture.coordinate.size >= 2) {
                    val position = LatLng(picture.coordinate[0], picture.coordinate[1])
                    Marker(
                        state = MarkerState(position = position),
                        title = picture.commonName,
                        snippet = "Posted by: ${picture.postedBy.name}",
                        onClick = {
                            navController.navigate("detail/$index")
                            true
                        }
                    )
                }
            }
        }
    }
}
