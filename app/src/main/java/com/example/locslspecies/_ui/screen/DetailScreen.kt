package com.example.locslspecies._ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.runtime.DisposableEffect
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
import com.example.locslspecies.viewmodel.AuthViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

// Les données d'une plante de la liste de plantes de l'utilisateur
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navBackStackEntry: NavBackStackEntry) {
    val viewModel: AuthViewModel = viewModel()
    val position = navBackStackEntry.arguments?.getInt("position") ?: -1
    val pictures by viewModel.pictures.observeAsState(emptyList())
    var commentText by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf<Comment>()) }
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val documentReference by viewModel.userDocumentRef.observeAsState()

    DisposableEffect(Unit) {
        viewModel.fetchImages()
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        pictures.getOrNull(position)?.let { picture ->
            Image(
                painter = rememberAsyncImagePainter(picture.url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Poste par: ${picture.postedBy.surname}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Date: ${picture.postedAt.toDate().toString()}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Nom: ${picture.scientificName}", fontSize = 14.sp, color = Color.Black)
                Text(
                    text = "Nom scientifique: ${picture.commonName}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(text = "Famille: ${picture.family}", fontSize = 14.sp, color = Color.Black)
            }
            // Spacer(modifier = Modifier.weight(1f))
            Divider(
                color = Color(0xFF3B808B),
                thickness = 2.dp,
            )

            LazyColumn(state = scrollState, modifier = Modifier.weight(12f)) {
                items(comments.size) { index ->
                    val comment = comments[index]

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(comment.user.url),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(50)) // Circular image
                            )
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
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
                                Text(text = comment.user.name, fontWeight = FontWeight.Bold)
                                Text(text = comment.text)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Ajouter un commentaire...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                //.focusRequester(focusRequester),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (commentText.isNotBlank()) {
                    comments = comments + Comment(commentText, user)
                    commentText = ""
                    focusManager.clearFocus()
                }
            }),
            trailingIcon = {
                IconButton(onClick = {
                    if (commentText.isNotBlank()) {
                        comments = comments + Comment(commentText, user)

                        focusManager.clearFocus()
                        val comment = Comments(
                        text = commentText,
                        commentedByRef = documentReference as DocumentReference,
                        commentedAt = Timestamp.now(),
                        )
                        viewModel.addComment(comment, position)
                        commentText = ""
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_send_24), // Replace with your send icon resource
                        contentDescription = "envoi",
                        modifier = Modifier.size(24.dp),

                    )
                }
            }
        )

    }
}


data class Comment(
    val text: String,
    val user: currentUser // Assuming User class has properties like name and profilePictureUrl
)
data class currentUser(
    val name: String,
    val url: String // Assuming User class has properties like name and profilePictureUrl
)
val user = currentUser("John Doe", "https://avatars.githubusercontent.com/u/77149638?v=4")