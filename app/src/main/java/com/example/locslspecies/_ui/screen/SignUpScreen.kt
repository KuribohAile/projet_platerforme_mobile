package com.example.locslspecies._ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies.model._User
import com.example.locslspecies._ui.navigation.Route
import com.example.locslspecies.helper.ErrorHandling
import com.example.locslspecies.controller.AuthUiState
import com.example.locslspecies.controller.AuthViewModel

// Fonction qui permet de créer la page d'inscription de l'utilisateur.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    // Contexte local nécessaire pour certaines opérations.
    val context = LocalContext.current
    // Initialisation du ViewModel.
    val viewModel: AuthViewModel = viewModel()
    // Observation de l'état de l'interface utilisateur pour les erreurs et les messages.
    val uiState by viewModel.uiState.collectAsState()
    // État de l'utilisateur en cours de création.
    var user by remember { mutableStateOf(_User()) }

    // Vérification de l'état de l'inscription et navigation en cas de succès.
    when (uiState) {
        AuthUiState.Success -> {
            navController.navigate(Route.SignIn.screen_route)
        }
        else -> {}
    }

    // Disposition en colonne pour le formulaire d'inscription.
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
        // Sous-titre "Inscription".
        Text("Inscription", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))

        // Champs de texte personnalisés pour les informations de l'utilisateur.
        CustomOutlinedTextField(
            value = user.name,
            onValueChange = { newText -> user = user.copy(name = newText) },
            label = "Votre nom"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = user.email,
            onValueChange = { newText -> user = user.copy(email = newText) },
            label = "Votre email"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = user.password,
            onValueChange = { newText -> user = user.copy(password = newText) },
            label = "Votre mot de passe"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            value = user.repeatPassword,
            onValueChange = { newText -> user = user.copy(repeatPassword = newText) },
            label = "Repetez votre mot de passe"
        )
        // Espaceur.
        Spacer(modifier = Modifier.height(30.dp))

        // Bouton pour soumettre le formulaire d'inscription.
        Button(onClick = {
            // Vérification de la correspondance des mots de passe.
            if (user.password != user.repeatPassword){
                Toast.makeText(
                    context,
                    "Les mots de passe ne correspondent pas",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Envoi des données d'inscription au ViewModel.
                viewModel.register(user)
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B808B),
                contentColor = Color.White)
        ) {
            Text("S'inscrire", color = Color.White)
        }
        // Espaceur.
        Spacer(modifier = Modifier.height(15.dp))
        // Gestion des erreurs de l'interface utilisateur.
        ErrorHandling(uiState)
    }
}

// Customisation du champ de texte pour l'inscription.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
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
