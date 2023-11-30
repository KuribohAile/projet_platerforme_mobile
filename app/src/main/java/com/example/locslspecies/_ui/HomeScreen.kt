package com.example.locslspecies._ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.model.UsersPictures

// Les données de la liste de plantes de l'utilisateur
@Composable
fun HomeScreen(usersPictures: List<UsersPictures>) {
    val context = LocalContext.current
    LazyColumn {
        items(usersPictures) { plant ->
            HomeScreen(
                imageUrl = plant.imageUrl,
                photographer = plant.photographer,
                date = plant.date,
                scientificName = plant.scientificName,
                commonName = plant.commonName,
                family = plant.family,
                context = context
            )
        }
    }
}

// Les données d'une plante de la liste de plantes de l'utilisateur
@Composable
fun HomeScreen(

    imageUrl: String,
    photographer: String,
    date: String,
    scientificName: String,
    commonName: String,
    family: String,
    context: Context
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
            Text(
                text = photographer,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = date,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = scientificName, fontSize = 18.sp, color = Color.Black)
                    Text(text = commonName, fontSize = 14.sp, color = Color.Black)
                    Text(text = family, fontSize = 14.sp, color = Color.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Button(onClick = { Toast.makeText(context, "pas encore implemente", Toast.LENGTH_LONG).show() }) {
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