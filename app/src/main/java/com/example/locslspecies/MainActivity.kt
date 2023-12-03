package com.example.locslspecies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.locslspecies._ui.navigation.BottomBar
import com.example.locslspecies.model.UsersPictures
import com.example.locslspecies._ui.navigation.NavHost
import com.example.locslspecies.model.UserPictures
import com.example.locslspecies.ui.theme.LocslSpeciesTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore

        // liste d'exemple de plantes de tous les utilisateurs
        val AllUserslants = getUserPictures()

        // liste d'exemple de plantes photographiées par l'utilisateur
        val Userplants = getUsersPictures()
        setContent {
            LocslSpeciesTheme {

                val navController: NavHostController = rememberNavController()
                val bottomBarHeight = 56.dp
                val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }

                var buttonsVisible = remember { mutableStateOf(true) }

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            state = buttonsVisible,
                            modifier = Modifier
                        )
                    }) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavHost(navController = navController, userPictures = AllUserslants)
                    }
                }
            }
        }
    }
}

fun getUsersPictures(): List<UsersPictures> {

    return listOf(

        UsersPictures(
            imageUrl = "https://networkofnature.org/userContent/ecom/products/2626-7159/finals/7159%5FABIEBAL%5F533%2Ejpg",
            photographer = "Eleonora Quartana",
            date = "19 nov. 2023",
            scientificName = "Nom scientifique: Myrtus",
            commonName = "Nom commun: Myrte",
            family = "Famille: Myrtaceae"
        ),

        UsersPictures(
            imageUrl = "https://networkofnature.org/userContent/ecom/products/2626-7159/finals/7159%5FABIEBAL%5F533%2Ejpg",
            photographer = "Eleonora Quartana",
            date = "19 nov. 2023",
            scientificName = "Nom scientifique: Myrtus",
            commonName = "Nom commun: Myrte",
            family = "Famille: Myrtaceae"
        ),
        UsersPictures(
            imageUrl = "https://networkofnature.org/userContent/ecom/products/2626-7159/finals/7159%5FABIEBAL%5F533%2Ejpg",
            photographer = "Eleonora Quartana",
            date = "19 nov. 2023",
            scientificName = "Nom scientifique: Myrtus",
            commonName = "Nom commun: Myrte",
            family = "Famille: Myrtaceae"
        ),
    )
}

fun getUserPictures(): List<UserPictures> {

    return listOf(
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante"),
        UserPictures(R.drawable.plante, "Nom plante")
    )
}