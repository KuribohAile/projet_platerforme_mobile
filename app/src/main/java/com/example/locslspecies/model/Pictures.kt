package com.example.locslspecies.model

import com.google.firebase.Timestamp

data class Pictures(
    val comments: String,
    val commonName: String,
    val family: String,
    val scientificName: String,
    val localization: List<Double>,
    val locatedIn: List<Pair<Double, Double>>,
    val postedAt: Timestamp,
    val postedBy: String,
    val url: String,
    val validation: Int
)