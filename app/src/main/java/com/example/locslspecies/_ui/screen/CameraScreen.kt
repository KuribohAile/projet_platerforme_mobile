package com.example.locslspecies._ui.screen
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.locslspecies.helper.requestPermission
import com.example.locslspecies.model.ApiResponse
import com.example.locslspecies.viewmodel.AuthViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

// fonction qui permet de prendre une photo
@SuppressLint("SuspiciousIndentation")
@Composable
fun CameraScreen(navBackStackEntry: NavBackStackEntry, navController: NavHostController) {

    val viewModel: AuthViewModel = viewModel()
    val documentReference by viewModel.userDocumentRef.observeAsState()
    val imageLabels = remember { mutableStateListOf<String>() }
    var response by remember { mutableStateOf<ApiResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val image: InputImage
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

        Column {
            Image(
                modifier = Modifier
                    .padding(70.dp, 80.dp)
                    .height(300.dp),
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentDescription = null
            )
            Column(modifier = Modifier.fillMaxSize(), // Fill the parent
                horizontalAlignment = Alignment.CenterHorizontally, // Center children horizontally
                verticalArrangement = Arrangement.Center) {
                imageLabels.forEach { label ->
                    Text(text = label, color = Color.Black)
                }
            }

        }

        try {
            image = InputImage.fromFilePath(context, capturedImageUri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                        val text = labels.get(0).text
                        val confidence = labels.get(0).confidence
                        if (!(imageLabels.size >= 2)) {
                            imageLabels.add(text)
                            imageLabels.add(confidence.toString())
                        }
                        Log.d("MYTAGZ", "CameraScreen: $text")
                        Log.d("MYTAGZ", "CameraScreen: $confidence")
                }
                .addOnFailureListener { e ->
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

      LaunchedEffect(Unit) {

          viewModel.pictureRecognition(capturedImageUri)

    }

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
