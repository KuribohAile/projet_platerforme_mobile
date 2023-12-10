package com.example.locslspecies._ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies._ui.navigation.Route
import com.example.locslspecies.helper.ErrorHandling
import com.example.locslspecies.controller.AuthViewModel

// Fonction qui permet de créer la page de connexion de l'utilisateur.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController) {
    // Initialisation du ViewModel.
    val viewModel: AuthViewModel = viewModel()
    // État du champ email.
    val email = remember { mutableStateOf("") }
    // État du champ mot de passe.
    val password = remember { mutableStateOf("") }
    // Observation de l'état de connexion de l'utilisateur.
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)
    // Observation de l'état de l'interface utilisateur pour les erreurs et les messages.
    val uiState by viewModel.uiState.collectAsState()

    // Disposition en colonne pour le formulaire de connexion.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003654))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre de l'application.
        Text("Local Species", color = Color.White, style = MaterialTheme.typography.headlineLarge)
        // Espaceur.
        Spacer(modifier = Modifier.height(48.dp))
        // Sous-titre "Se connecter".
        Text("Se connecter", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))

        // Champ de texte personnalisé pour l'email.
        CustomOutlinedTextFieldSignIn(
            value = email.value,
            onValueChange = { newText -> email.value = newText },
            label = "Votre email"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))

        // Champ de texte personnalisé pour le mot de passe.
        CustomOutlinedTextFieldSignIn(
            value = password.value,
            onValueChange = { newText -> password.value = newText },
            label = "Votre mot de passe"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(50.dp))
        // Ligne pour les boutons d'action.
        Row {
            // Bouton pour naviguer vers la page d'inscription.
            Button(onClick = { navController.navigate(Route.SignUp.screen_route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B808B), // Couleur du fond du bouton
                    contentColor = Color.White)
            ) {
                Text("S'inscrire", color = Color.White)
            }
            // Espaceur.
            Spacer(modifier = Modifier.width(16.dp))
            // Bouton pour se connecter.
            Button(onClick = {
                // Vérification si les champs sont vides.
                if(email.value == "" || password.value == "") {
                    Toast.makeText(
                        navController.context,
                        "Veuillez remplir tous les champs svp",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Tentative de connexion.
                    viewModel.login(email.value, password.value)
                    if (isLoggedIn){
                        // Navigation vers la page d'accueil si la connexion est réussie.
                        navController.navigate(Route.Home.screen_route)
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B808B), // Couleur du fond du bouton
                    contentColor = Color.White)
            ) {
                Text("Connexion", color = Color.White)
            }
        }

        // Affichage des erreurs si nécessaire.
        ErrorHandling(uiState)
    }
}

// Fonction de Composable pour personnaliser le champ de texte.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldSignIn(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    // Champ de texte avec bordure personnalisée.
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF03C5E4)) },
        shape = RoundedCornerShape(16.dp),
        // Transformation visuelle pour masquer le mot de passe.
        visualTransformation = if (label == "Votre mot de passe" || label == "Repetez votre mot de passe") PasswordVisualTransformation() else VisualTransformation.None,
        // Personnalisation des couleurs du champ de texte.
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF03C5E4),
            unfocusedBorderColor = Color(0xFF03C5E4),
        )
    )
}



