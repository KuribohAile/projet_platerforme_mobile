package com.example.locslspecies._ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies.model._User
import com.example.locslspecies.R
import com.example.locslspecies._ui.navigation.Route
import com.example.locslspecies.helper.ErrorHandling
import com.example.locslspecies.model.AuthUiState
import com.example.locslspecies.viewmodel.AuthViewModel

// fonction qui permet de créer la page de d'inscription de l'utilisateur
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var user by remember { mutableStateOf(_User()) }

    // on verifie l'etat de l'inscription et on affiche un message en fonction
    when (uiState) {
        AuthUiState.Success -> {
            navController.navigate(Route.SignIn.screen_route) }
        else -> {}
    }

    // on definit les champs de texte pour l'inscription
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003654))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Local Species", color = Color.White, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(48.dp))
        Text("Inscription", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = user.surname,
            onValueChange = { newText -> user = user.copy(surname = newText) },
            label = "Votre prenom"
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = user.name,
            onValueChange = { newText -> user = user.copy(name = newText) },
            label = "Votre nom"
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = user.email,
            onValueChange = { newText -> user = user.copy(email = newText) },
            label = "Votre email"
        )

        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = user.description,
            onValueChange = { newText -> user = user.copy(description = newText) },
            label = "Decrivez vous"
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = user.password,
            onValueChange = { newText -> user = user.copy(password = newText) },
            label = "Votre mot de passe",

        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = user.repeatPassword,
            onValueChange = { newText -> user = user.copy(repeatPassword = newText) },
            label = "Repetez votre mot de passe"
        )
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            if (user.password != user.repeatPassword){
                Toast.makeText(
                    navController.context,
                    "Les mots de passe ne correspondent pas",
                    Toast.LENGTH_LONG
                ).show()

            }else {
                viewModel.register(user)
            }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B808B),
                contentColor = Color.White)
        ) {
            Text("S'inscrire", color = Color.White)
        }
        Spacer(modifier = Modifier.height(15.dp))
        ErrorHandling(uiState)
/*        Spacer(modifier = Modifier.height(40.dp))
        Text("Se connecter via", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        IconButton(onClick = { *//* Handle Google Sign-In here *//* }) {
            Icon(
                painter = painterResource(id = R.drawable.google_sign_in),
                contentDescription = "Google Sign-In"
            )
        }*/

    }
}

// customisation du champ de texte pour l'inscription
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF03C5E4)) },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (label == "Votre mot de passe" || label == "Repetez votre mot de passe") PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF03C5E4),
            unfocusedBorderColor = Color(0xFF03C5E4),
        )
    )
}
