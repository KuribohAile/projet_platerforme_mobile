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
import com.example.locslspecies.viewmodel.AuthViewModel

// fonction qui permet de créer une liste de plantes de tous les utilisateurs
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navBackStackEntry: NavBackStackEntry, navController: NavHostController) {
    val viewModel: AuthViewModel = viewModel()
    val pictures by viewModel.pictures.observeAsState(emptyList())

    Column {
        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize()
                .weight(1f)
        ) {
            Text(
                text = "Gallerie",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(24.dp)
            )
        }

        Row(Modifier.weight(6f)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(pictures) { picture ->
                    PictureCard(picture = picture) {
                        // Action on card click - navigate to a detail screen
                        val position = pictures.indexOf(picture)
                        navController.navigate("detail/$position")
                    }
                }
            }
        }
    }
}

// PictureCard composable with a clickable modifier
@Composable
fun PictureCard(picture: Pictures, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick), // Add clickable modifier here
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier.background(Color(0xFF3B808B)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(picture.url),
                contentDescription = "Image of ${picture.commonName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Text(
                text = picture.commonName,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
