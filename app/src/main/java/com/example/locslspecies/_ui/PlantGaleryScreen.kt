package com.example.locslspecies._ui
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// fonction qui permet de créer une liste de plantes de tous les utilisateurs
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantGalleryScreen(plants: List<Plant>) {

    Column {
        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize().weight(1f)
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
                items(plants) { plant ->
                    PlantCard(plant)
                }
            }

        }


    }
}

// fonction qui permet de créer une carte pour chaque plante
@Composable
fun PlantCard(plant: Plant) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(Modifier.background(Color(0xFF3B808B)),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = plant.imageResourceId),
                contentDescription = "Image of ${plant.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Text(
                text = plant.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

// classe qui permet de stocker les données d'une plante
data class Plant(val imageResourceId: Int, val name: String)
