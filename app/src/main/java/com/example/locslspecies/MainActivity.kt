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
import com.example.locslspecies._ui.navigation.NavHost
import com.example.locslspecies.ui.theme.LocslSpeciesTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Classe principale de l'activité, point d'entrée de l'application.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation de la base de données Firebase Firestore.
        val db = Firebase.firestore

        // Définition du contenu de l'interface utilisateur avec Jetpack Compose.
        setContent {
            // Application du thème de l'application.
            LocslSpeciesTheme {
                // Création d'un contrôleur de navigation pour gérer la navigation dans l'application.
                val navController: NavHostController = rememberNavController()

                // État pour gérer la visibilité des boutons de la barre de navigation.
                var buttonsVisible = remember { mutableStateOf(true) }

                // Structure de l'application avec une barre de navigation en bas.
                Scaffold(
                    bottomBar = {
                        // Appel de la fonction BottomBar qui crée la barre de navigation en bas.
                        BottomBar(
                            navController = navController,
                            state = buttonsVisible,
                            modifier = Modifier
                        )
                    }) { paddingValues ->
                    // Conteneur pour le contenu principal de l'application.
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        // Hôte de navigation pour gérer les différents écrans de l'application.
                        NavHost(navController = navController)
                    }
                }
            }
        }
    }
}
