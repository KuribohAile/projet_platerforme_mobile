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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Les données d'une plante de la liste de plantes de l'utilisateur
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navBackStackEntry: NavBackStackEntry) {

    val viewModel: AuthViewModel = viewModel()
    val idPicture = navBackStackEntry.arguments?.getString("idPicture") ?: ""
    val pictures by viewModel.pictures.observeAsState(emptyList())
    val users by viewModel.users.observeAsState(emptyList())
    val idUser by viewModel.userId.observeAsState()
    var commentText by remember { mutableStateOf("") }
    val comments by viewModel.comments.observeAsState(emptyList())
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val documentReference by viewModel.userDocumentRef.observeAsState()
    val picture = pictures.find { picture -> picture.id == idPicture }
    val userName = users.find { user -> user.id == picture?.idUser }?.name


    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {

        Image(
            painter = rememberAsyncImagePainter(picture?.url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
        Column(modifier = Modifier.padding(4.dp)) {
            Column(modifier = Modifier.padding(1.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Poste par: ${userName.toString()}",
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
            //Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nom: ${picture?.scientificName}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "Nom scientifique: ${picture?.commonName}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(text = "Famille: ${picture?.family}", fontSize = 14.sp, color = Color.Black)
        }
            // Spacer(modifier = Modifier.weight(1f))
            Divider(
                color = Color(0xFF3B808B),
                thickness = 2.dp,
            )

            LazyColumn(state = scrollState, modifier = Modifier.weight(8f)) {
                    items(comments.size) { index ->
                        val comment = comments[index]
                        val idUser = comments.find {comment  -> comment.idPicture == idPicture }?.idUser
                        if (comment.idPicture == idPicture){

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(users.find {user  -> user.id == comment.idUser }?.imageProfileUrl),
                                    contentDescription = "Profile picture",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(50)) // Circular image
                                )
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
                                        Text(
                                            text = users.find {user  -> user.id == comment.idUser }?.name.toString(),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(text = comment.text)
                                    }
                                }
                            }

                        }

                    }
                }

            Spacer(modifier = Modifier.weight(0.5f))

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
                trailingIcon = {
                    IconButton(onClick = {
                        if (commentText.isNotBlank()) {
                            // comments = comments + Comment(commentText, user)

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
                            painter = painterResource(id = R.drawable.baseline_send_24), // Replace with your send icon resource
                            contentDescription = "envoi",
                            modifier = Modifier.size(24.dp),

                            )
                    }
                }
            )

        }

}