package com.example.locslspecies._ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import com.example.locslspecies.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

// Les données de la liste de plantes de l'utilisateur
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val usersPictures by viewModel.pictures.observeAsState(emptyList())
    val users by viewModel.users.observeAsState(emptyList())
    val userId by viewModel.userId.observeAsState()



    DisposableEffect(Unit) {
        viewModel.PictureRecognitionBasedOnComments()
        viewModel.fetchImages()
        viewModel.fetchUsers()

        onDispose {

        }
    }

    LazyColumn {
        items(usersPictures) { picture ->
            users.find { user -> user.id == picture.idUser }?.name?.let {
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

                    )
            }
            Divider(
                color = Color(0xFF3B808B),
                thickness = 2.dp,
            )
        }

    }

}

// Les données d'une plante de la liste de plantes de l'utilisateur
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
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Row ( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = "Poste par: $postedBy",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date),
                    fontSize = 12.sp,
                    color = Color.Gray,

                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Nom: $commonName", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Nom scientifique $scientificName", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Famille: $family", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Valider par: $validedBy utiisateurs", fontSize = 14.sp, color = Color.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Button(onClick = {

                        navController.navigate("detail/$idPicture") {
                            popUpTo("detail/$idPicture") { inclusive = true }
                        }

                    }) {
                        Text("DETAILS")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(onClick = { Toast.makeText(context, "pas encore implemente", Toast.LENGTH_LONG).show() }) {
                        Text("VALIDER")

                    }
                }
            }
        }
    }
}