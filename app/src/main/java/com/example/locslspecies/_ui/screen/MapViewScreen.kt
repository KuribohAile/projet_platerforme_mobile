package com.example.locslspecies._ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies.controller.AuthViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Fonction qui affiche la carte avec les emplacements des plantes.
@Composable
fun MapViewScreen(navController: NavHostController) {
    // Initialisation du ViewModel.
    val viewModel: AuthViewModel = viewModel()
    // Observation des images des plantes.
    val pictures by viewModel.pictures.observeAsState(emptyList())

    // État de la position de la caméra sur la carte, centrée sur des coordonnées par défaut.
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.4149, -71.0489), 10f)
    }

    // Disposition en colonne pour tout l'écran.
    Column(modifier = Modifier.fillMaxSize()) {
        // Composable de la carte Google Map.
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Pour chaque image de plante, création d'un marqueur sur la carte.
            pictures.forEachIndexed { index, picture ->
                // Vérification que la plante a des coordonnées valides.
                if (picture.coordinate.size >= 2) {
                    // Création de la position à partir des coordonnées.
                    val position = LatLng(picture.coordinate[0], picture.coordinate[1])
                    // Ajout du marqueur sur la carte.
                    Marker(
                        state = MarkerState(position = position),
                        title = picture.commonName,
                        snippet = "Posté par : ",
                        // Lors du clic sur le marqueur, navigation vers l'écran de détail de la plante.
                        onClick = {
                            navController.navigate("detail/${picture.id}")
                            true // Indique que l'événement de clic a été géré.
                        }
                    )
                }
            }
        }
    }
}
