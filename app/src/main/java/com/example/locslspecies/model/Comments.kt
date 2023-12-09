package com.example.locslspecies.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference


data class Comments(
    val id : String = "",
    val text : String = "",
    val commentedAt : Timestamp = Timestamp.now(),
    val idPicture: String = "",
    val idUser: String = "",
    )