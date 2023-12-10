package com.example.locslspecies._ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.R
import com.example.locslspecies.model.Comments
import com.example.locslspecies.controller.AuthViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Les données d'une plante de la liste de plantes de l'utilisateur
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navBackStackEntry: NavBackStackEntry) {

    // Initialisation du ViewModel et récupération des données passées.
    val viewModel: AuthViewModel = viewModel()
    val idPicture = navBackStackEntry.arguments?.getString("idPicture") ?: ""
    val pictures by viewModel.pictures.observeAsState(emptyList())
    val users by viewModel.users.observeAsState(emptyList())
    val idUser by viewModel.userId.observeAsState()
    var commentText by remember { mutableStateOf("") }
    val comments by viewModel.comments.observeAsState(emptyList())
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    // Trouver l'image correspondante par son ID.
    val picture = pictures.find { it.id == idPicture }
    // Trouver le nom d'utilisateur correspondant à l'ID de l'utilisateur.
    val userName = users.find { it.id == picture?.idUser }?.name

    // Disposition en colonne pour l'écran des détails.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        // Affichage de l'image de la plante.
        Image(
            painter = rememberAsyncImagePainter(picture?.url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
        // Informations sur la plante.
        Column(modifier = Modifier.padding(4.dp)) {
            Column(modifier = Modifier.padding(1.dp)) {
                // Informations sur l'utilisateur qui a posté et la date de publication.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Posté par: ${userName.toString()}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                            picture?.postedAt?.toDate() ?: Date()
                        ),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            // Nom scientifique de la plante.
            Text(
                text = "Nom: ${picture?.scientificName}",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
        // Séparateur visuel.
        Divider(
            color = Color(0xFF3B808B),
            thickness = 2.dp,
        )

        // Liste des commentaires associés à l'image de la plante.
        LazyColumn(state = scrollState, modifier = Modifier.weight(8f)) {
            items(comments.size) { index ->
                val comment = comments[index]
                if (comment.idPicture == idPicture){
                    // Disposition pour chaque commentaire.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image de profil de l'utilisateur qui a fait le commentaire.
                        Image(
                            painter = rememberAsyncImagePainter(users.find { it.id == comment.idUser }?.imageProfileUrl),
                            contentDescription = "Photo de profil",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50)) // Image circulaire
                        )
                        // Carte élevée contenant le commentaire.
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                // Nom de l'utilisateur ayant écrit le commentaire.
                                Text(
                                    text = users.find { it.id == comment.idUser }?.name.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                                // Texte du commentaire.
                                Text(text = comment.text)
                            }
                        }
                    }
                }
            }
        }

        // Espaceur pour aligner le champ de texte de commentaire.
        Spacer(modifier = Modifier.weight(0.5f))

        // Champ de texte pour écrire un commentaire.
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Ajouter un commentaire...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                // Actions à effectuer lorsque l'utilisateur valide son commentaire.
                if (commentText.isNotBlank()) {
                    // Création et envoi du commentaire.
                    val comment = Comments(
                        idUser = idUser!!,
                        idPicture = idPicture,
                        id = UUID.randomUUID().toString(),
                        text = commentText,
                        commentedAt = Timestamp.now(),
                    )
                    viewModel.addComment(comment, idPicture)
                    viewModel.fetchComments()
                    commentText = ""
                    focusManager.clearFocus()
                }
            }),
            // Icône pour envoyer le commentaire.
            trailingIcon = {
                IconButton(onClick = {
                    // Actions à effectuer lorsque l'icône d'envoi est cliquée.
                    if (commentText.isNotBlank()) {
                        focusManager.clearFocus()
                        val comment = Comments(
                            idUser = idUser!!,
                            idPicture = idPicture,
                            id = UUID.randomUUID().toString(),
                            text = commentText,
                            commentedAt = Timestamp.now(),
                        )
                        viewModel.addComment(comment, idPicture)
                        viewModel.fetchComments()
                        commentText = ""
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_send_24), // Remplacer par la ressource de votre icône d'envoi
                        contentDescription = "envoi",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        )
    }
}
