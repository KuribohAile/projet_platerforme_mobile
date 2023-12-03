package com.example.locslspecies._ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.R
import com.example.locslspecies.viewmodel.AuthViewModel

// Les données d'une plante de la liste de plantes de l'utilisateur
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navBackStackEntry: NavBackStackEntry) {
    val viewModel: AuthViewModel = viewModel()
    val position = navBackStackEntry.arguments?.getInt("position") ?: -1
    val pictures by viewModel.pictures.observeAsState(emptyList())
    var commentText by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf<String>()) }

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
                Text(text = picture.postedBy.surname, fontSize = 12.sp, color = Color.Gray)
                Text(text = picture.postedAt.toDate().toString(), fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = picture.scientificName, fontSize = 18.sp, color = Color.Black)
                Text(text = picture.commonName, fontSize = 14.sp, color = Color.Black)
                Text(text = picture.family, fontSize = 14.sp, color = Color.Black)
            }
           // Spacer(modifier = Modifier.weight(1f))
            Divider(
                color = Color(0xFF3B808B),
                thickness = 2.dp,
            )

            comments.forEach { comment ->
                Text(text = comment, modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))


        // TextField for comments
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Add a comment...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (commentText.isNotBlank()) {
                    comments = comments + commentText
                    commentText = ""
                }
            }),
            trailingIcon = {
                IconButton(onClick = {
                    if (commentText.isNotBlank()) {
                        comments = comments + commentText
                        commentText = ""
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_send_24), // Replace with your send icon resource
                        contentDescription = "Send comment",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

    }
}


