package com.example.locslspecies._ui
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.locslspecies.R

// fonction qui permet de créer la page de profil de l'utilisateur
@Composable
fun ProfileScreen(onDisconnect: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize().weight(1f)
        ) {

            Text(
                text = "Profile",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(24.dp)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.profile_24),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text("John Doe", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Paysan",fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("j'habite dans un petit village niché au creux des montagnes verdoyantes, j'aime s'adonner" +
                " aux activités comme la pêche ou le travail des champs," +
                " j'aime aussi passer mon temps libre dans les vastes étendues de nature qui entoure notre village.\n" +
                "\n",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Divider()
        Spacer(modifier = Modifier.height(20.dp))
        Text("Interet", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Chip(label = "Photographie")
            Chip(label = "Musique")
            Chip(label = "chasse")
            Chip(label = "Nature")
        }
        Spacer(modifier = Modifier.height(150.dp))

        // bouton qui permet de se deconnecter l'utilisateur
        Button(onClick = onDisconnect,  colors = ButtonDefaults.buttonColors(Color(0xFF2D9AAC)), modifier = Modifier
            .padding(horizontal = 32.dp)) {
            Text("Deconnecter", color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// fonction qui permet de créer un chip
@Composable
fun Chip(label: String) {
    Surface(
        modifier = Modifier.border(1.dp, Color.Black, CircleShape),
        color = Color(0xFF3B808B),
        shape = CircleShape,
        shadowElevation = 4.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}
