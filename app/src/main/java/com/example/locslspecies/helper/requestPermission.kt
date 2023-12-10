package com.example.locslspecies.helper

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

// Fonction Composable pour demander les permissions de la caméra et de localisation.
@Composable
fun requestPermission(
    context: Context,
    uri: Uri,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>
): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
    // Lanceur combiné de permissions pour la caméra et la localisation.
    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Vérification si la permission de la caméra a été accordée.
        val isCameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
        // Vérification si une des permissions de localisation a été accordée.
        val isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        // Actions en fonction des permissions accordées.
        when {
            // Si toutes les permissions nécessaires sont accordées.
            isCameraPermissionGranted && isLocationPermissionGranted -> {
                // Lancement de l'appareil photo.
                cameraLauncher.launch(uri)
                // Récupération de la localisation.
                retrieveLocation(context)
            }
            // Si la permission de la caméra est refusée.
            !isCameraPermissionGranted -> Toast.makeText(context, "Permission Caméra Refusée", Toast.LENGTH_SHORT).show()
            // Si la permission de localisation est refusée.
            !isLocationPermissionGranted -> Toast.makeText(context, "Permission Localisation Refusée", Toast.LENGTH_SHORT).show()
        }
    }

    // Retourne le lanceur de permissions.
    return permissionsLauncher
}
