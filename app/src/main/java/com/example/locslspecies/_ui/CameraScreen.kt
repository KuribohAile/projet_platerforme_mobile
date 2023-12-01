package com.example.locslspecies._ui
import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.helper.requestPermission
import com.example.locslspecies.model.GeoPoint
import com.example.locslspecies.model.Pictures
import com.example.locslspecies.model._User
import com.example.locslspecies.viewmodel.AuthViewModel
import com.google.firebase.Timestamp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

// fonction qui permet de prendre une photo
@Composable
fun CameraScreen() {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.example.locslspecies" + ".provider", file
    )
    // variable qui permet de stocker l'image
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) capturedImageUri = uri
    }
    val permissionsLauncher = requestPermission(context, uri, cameraLauncher = cameraLauncher)


    // interface graphique de la page Camera
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp)
        ) {

            Text(

                text = "Camera",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,

            )
        }
        Spacer(modifier = Modifier.height(500.dp))
        Row(Modifier.weight(1f)) {
            // bouton qui permet de prendre une photo
            Button(onClick = {

                permissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                )
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B808B), // Use primary color for the button background
                contentColor = Color.White)) {
                Text(text = "Prendre une photo")
            }

        }

    }

    // affichage de l'image prise
    if (capturedImageUri.path?.isNotEmpty() == true) {

        Image(
            modifier = Modifier
                .padding(70.dp, 80.dp)
                .height(300.dp),
            painter = rememberAsyncImagePainter(capturedImageUri),
            contentDescription = null
        )

        viewModel.uploadImage(capturedImageUri, onSuccess = {
           // Log.d("MYTAG", "CameraScreen: $it")
            val picture = Pictures(
                url = it.toString(),
                postedAt = Timestamp(Date()),
                scientificName = "scientificName",
                commonName = "commonName",
                family = "family",
                comments = "",
                localization = listOf(GeoPoint.latitude, GeoPoint.longitude),
                locatedIn = listOf( Pair(0.0,0.0)),
                validation = 0,
                postedBy = viewModel.userId.value.toString(),
                )

                 viewModel.insertUserPicture(picture = picture)

        }, onFailure = {

        })
    }


}
// fonction qui permet de creer un fichier pour stocker l'image
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}
