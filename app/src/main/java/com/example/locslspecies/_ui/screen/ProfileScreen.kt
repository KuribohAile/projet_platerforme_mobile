package com.example.locslspecies._ui.screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.controller.AuthViewModel

// Fonction qui permet de créer la page de profil de l'utilisateur.
@Composable
fun ProfileScreen(onDisconnect: () -> Unit) {
    // Initialisation du ViewModel.
    val viewModel: AuthViewModel = viewModel()
    // Observation de la liste des utilisateurs.
    val users by viewModel.users.observeAsState(emptyList())
    // Observation de l'ID de l'utilisateur courant.
    val userId by viewModel.userId.observeAsState()
    // Filtrage pour obtenir les informations de l'utilisateur courant.
    val user = users.filter { it.id == userId }

    // Colonne principale pour la mise en page de l'écran du profil.
    Column {
        // En-tête de la page de profil.
        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize()
                .weight(0.5f)
        ) {
            Text(
                text = "Profil",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(15.dp)
            )
        }

        // Contenu de la page de profil.
        Column(
            Modifier.weight(4f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Espaceur.
            Spacer(modifier = Modifier.height(10.dp))
            // Image de profil de l'utilisateur.
            Image(
                painter = rememberAsyncImagePainter(user.getOrNull(0)?.imageProfileUrl),
                contentDescription = "Photo de profil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Pour rendre l'image ronde.
                    .border(2.dp, Color.Gray, CircleShape), // Ajout d'une bordure.
                contentScale = ContentScale.Crop
            )
            // Espaceur.
            Spacer(modifier = Modifier.height(10.dp))
            // Affichage du nom de l'utilisateur.
            user.getOrNull(0)
                ?.let { Text(it.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge) }
            // Espaceur.
            Spacer(modifier = Modifier.height(4.dp))
            // Affichage de la profession de l'utilisateur.
            Text("Paysan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            // Espaceur.
            Spacer(modifier = Modifier.height(10.dp))
            // Description de l'utilisateur.
            Text("j'habite dans un petit village niché au creux des montagnes verdoyantes, j'aime s'adonner" +
                    " aux activités comme la pêche ou le travail des champs," +
                    " j'aime aussi passer mon temps libre dans les vastes étendues de nature qui entoure notre village.\n" +
                    "\n",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp))

            // Séparateur visuel.
            Divider()

            // Section des intérêts de l'utilisateur.
            Text("Intérêt", style = MaterialTheme.typography.labelLarge)
            // Espaceur.
            Spacer(modifier = Modifier.height(10.dp))

            // Ligne contenant des puces (chips) des intérêts.
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Chip(label = "Photographie")
                Chip(label = "Musique")
                Chip(label = "Chasse")
            }
            // Espaceur.
            Spacer(modifier = Modifier.height(30.dp))

            // Bouton qui permet à l'utilisateur de se déconnecter.
            Button(onClick = onDisconnect, colors = ButtonDefaults.buttonColors(Color(0xFF2D9AAC)), modifier = Modifier
                .padding(horizontal = 32.dp)) {
                Text("Déconnecter", color = Color.White)
            }
        }
    }
}

// Fonction qui permet de créer un chip (étiquette).
@Composable
fun Chip(label: String) {
    // Surface pour le chip avec un effet d'ombre.
    Surface(
        modifier = Modifier.border(1.dp, Color.Black, CircleShape),
        color = Color(0xFF3B808B),
        shape = CircleShape,
        shadowElevation = 4.dp
    ) {
        // Texte à l'intérieur du chip.
        Text(
            text = label,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}

