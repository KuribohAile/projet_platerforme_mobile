package com.example.locslspecies._ui
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

// fonction qui permet de prendre une photo
@Composable
fun CameraScreen() {

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

    // fonction qui permet de lancer la camera
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    // fonction qui permet de demander la permission d'utiliser la camera
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // interface graphique de la page Camera
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier
                .background(Color(0xFF3B808B))
                .fillMaxSize().weight(1f)
                .padding(24.dp)
        ) {

            Text(

                text = "Camera",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,

            )
        }
        Spacer(modifier = Modifier.height(600.dp))
        Row(Modifier.weight(1f)) {
            // bouton qui permet de prendre une photo
            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
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
            modifier = Modifier.padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(capturedImageUri),
            contentDescription = null
        )
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