package com.example.locslspecies._ui.screen
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.model.Pictures
import com.example.locslspecies.controller.AuthViewModel

// Suppression de l'avertissement de l'utilisation inutilisée du paramètre de padding pour Scaffold dans Material3.
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
// Utilisation de l'API expérimentale Material3.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navBackStackEntry: NavBackStackEntry, navController: NavHostController) {
    // Initialisation du ViewModel pour l'authentification.
    val viewModel: AuthViewModel = viewModel()
    // Observation de la liste d'images du ViewModel.
    val pictures by viewModel.pictures.observeAsState(emptyList())
    // Observation de l'ID de l'utilisateur du ViewModel.
    val userId by viewModel.userId.observeAsState()
    // Filtrage des images pour ne récupérer que celles de l'utilisateur connecté.
    val filteredPictures = pictures.filter { it.idUser == userId }

    // Effet jetable pour appeler fetchImages une seule fois lors de la création du composant.
    DisposableEffect(Unit) {
        viewModel.fetchImages()
        onDispose {
            // Nettoyage si nécessaire lorsque le composant est détruit.
        }
    }

    // Disposition en colonne pour la galerie.
    Column {
        // En-tête de la galerie.
        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize()
                .weight(0.7f)
        ) {
            Text(
                text = "Galerie",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(15.dp)
            )
        }

        // Grille des images filtrées.
        Row(Modifier.weight(6f)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(filteredPictures) { picture ->
                    // Carte pour chaque image qui est cliquable.
                    PictureCard(picture = picture) {
                        // Action lors du clic sur la carte - navigation vers l'écran de détail.
                        val pictureId = picture.id
                        navController.navigate("detail/$pictureId")
                    }
                }
            }
        }
    }
}

// Composable PictureCard avec un modificateur cliquable
@Composable
fun PictureCard(picture: Pictures, onClick: () -> Unit) {
    // Carte qui contient l'image et le nom commun de la plante.
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick), // Modificateur pour rendre la carte cliquable.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier.background(Color(0xFF3B808B)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image de la plante.
            Image(
                painter = rememberAsyncImagePainter(picture.url),
                contentDescription = "Image de ${picture.commonName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            // Nom commun de la plante.
            Text(
                text = picture.commonName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
