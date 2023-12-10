package com.example.locslspecies._ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.controller.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Les données de la liste de plantes de l'utilisateur
@Composable
fun HomeScreen(navController: NavHostController) {
    // Contexte local nécessaire pour certaines opérations.
    val context = LocalContext.current
    // Initialisation du ViewModel.
    val viewModel: AuthViewModel = viewModel()
    // Observation des images des utilisateurs.
    val usersPictures by viewModel.pictures.observeAsState(emptyList())
    // Observation des utilisateurs.
    val users by viewModel.users.observeAsState(emptyList())

    // Effet jetable pour lancer des opérations de récupération des données.
    DisposableEffect(Unit) {
        viewModel.PictureRecognitionBasedOnComments()
        viewModel.fetchImages()
        viewModel.fetchUsers()

        // Fonction appelée lors de la suppression du composant.
        onDispose {

        }
    }

    // Disposition en colonne pour la page d'accueil.
    Column {
        // En-tête de la page d'accueil.
        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize()
                .weight(0.5f)

        ) {
            Text(
                text = "Accueil",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(15.dp)
            )
        }

        // Liste paresseuse pour afficher les images des utilisateurs.
        LazyColumn(
            Modifier.weight(4f),
            horizontalAlignment = Alignment.CenterHorizontally,) {
            items(usersPictures) { picture ->
                // Recherche du nom de l'utilisateur qui a posté l'image.
                users.find { user -> user.id == picture.idUser }?.name?.let {
                    // Elément de liste pour chaque image.
                    PictureListItem(
                        imageUrl = picture.url,
                        postedBy = it,
                        date = picture.postedAt.toDate(),
                        scientificName = picture.scientificName,
                        commonName = picture.commonName,
                        family = picture.family,
                        validedBy = picture.validation,
                        context = context,
                        navController = navController,
                        idPicture = picture.id,
                        onValidateClick = {
                            // Incrémentation du nombre de validations pour l'image.
                            viewModel.incrementValidationField(picture.id)
                        }
                    )
                }
                // Séparateur entre les éléments de la liste.
                Divider(
                    color = Color(0xFF3B808B),
                    thickness = 2.dp,
                )
            }
        }
    }
}

// Elément de liste pour une image de plante avec ses détails.
@Composable
fun PictureListItem(
    imageUrl: String,
    postedBy: String,
    date: Date,
    scientificName: String,
    commonName: String,
    validedBy: Int,
    family: String,
    context: Context,
    navController: NavHostController,
    idPicture: String,
    onValidateClick: () -> Unit
) {
    // Disposition en colonne pour chaque élément de liste.
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Image de la plante.
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        // Détails de l'image.
        Column(modifier = Modifier.padding(8.dp)) {
            // Nom de l'utilisateur et date de publication.
            Row ( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = "Posté par: $postedBy",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 14.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Informations sur la plante : nom commun, nombre de validations.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Nom: $commonName", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Validé par: $validedBy utilisateurs", fontSize = 14.sp, color = Color.Black)
                }
                // Boutons pour naviguer vers les détails ou valider l'image.
                Column(horizontalAlignment = Alignment.End) {
                    Button(onClick = {
                        // Navigation vers l'écran de détail de l'image.
                        navController.navigate("detail/$idPicture") {
                            popUpTo("detail/$idPicture") { inclusive = true }
                        }
                    }) {
                        Text("DETAILS")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(onClick = {
                        // Appel de la fonction de validation.
                        onValidateClick()
                    }) {
                        Text("VALIDER")
                    }
                }
            }
        }
    }
}
