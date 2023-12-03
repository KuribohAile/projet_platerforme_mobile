package com.example.locslspecies.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Pictures(
    val comments: DocumentReference? = null,
    val commonName: String = "",
    val family: String = "",
    val scientificName: String = "",
    val coordinate: List<Double> = listOf(),
    val postedAt: Timestamp = Timestamp.now(),
    val postedByRef: DocumentReference? = null,
    var postedBy: _User = _User(), // Assuming _User has a no-arg constructor
    val url: String = "",
    val validation: Int = 0
)