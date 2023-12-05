package com.example.locslspecies.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Pictures(
    val id: String = "",
    val idUser: String = "",
    val commonName: String = "",
    val family: String = "",
    val scientificName: String = "",
    val coordinate: List<Double> = listOf(),
    val postedAt: Timestamp = Timestamp.now(),
    val url: String = "",
    val validation: Int = 0
)