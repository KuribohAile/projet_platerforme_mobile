package com.example.locslspecies.model

// classe qui contient les données d'une plante de l'utilisateur
data class UsersPictures(
    val imageUrl: String,
    val photographer: String,
    val date: String,
    val scientificName: String,
    val commonName: String,
    val family: String
)