package com.example.locslspecies.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.locslspecies.model.Coordinate
import com.google.android.gms.location.LocationServices

// Fonction pour récupérer la localisation de l'utilisateur.
fun retrieveLocation(context: Context) {
    // Création d'une instance de FusedLocationProviderClient pour accéder aux services de localisation.
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Vérification des permissions de localisation.
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Si les permissions ne sont pas accordées, la fonction se termine sans action supplémentaire.
        return
    }

    // Demande de la dernière localisation connue.
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        // Vérification si la localisation est non nulle.
        if (location != null) {
            // Récupération de la latitude et de la longitude.
            val latitude = location.latitude
            val longitude = location.longitude

            // Traitement des données de localisation.
            // Ici, il semble que les coordonnées soient stockées dans un objet global `Coordinate`.
            // Assurez-vous que ce comportement est conforme à vos attentes et à votre architecture logicielle.
            Coordinate.latitude = latitude
            Coordinate.longitude = longitude
        }
    }
}

