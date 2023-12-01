package com.example.locslspecies.helper

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun requestPermission(
    context: Context,
    uri: Uri,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>
): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
    // Combined permission launcher for both camera and location
    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isCameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
        val isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        when {
            isCameraPermissionGranted && isLocationPermissionGranted -> {
                cameraLauncher.launch(uri)
                retrieveLocation(context)
            }
            !isCameraPermissionGranted -> Toast.makeText(context, "Permission Camera Refuse", Toast.LENGTH_SHORT).show()
            !isLocationPermissionGranted -> Toast.makeText(context, "Permission Location Refuse", Toast.LENGTH_SHORT).show()
        }
    }

    return permissionsLauncher
}