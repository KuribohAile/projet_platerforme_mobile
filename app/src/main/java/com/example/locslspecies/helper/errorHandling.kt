package com.example.locslspecies.helper

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.locslspecies.controller.AuthUiState

/**
 * Composant pour la gestion des erreurs liées à l'authentification.
 * Affiche des messages d'erreur personnalisés en fonction de 'uiState', et un indicateur de chargement pendant le traitement.
 */
@Composable
fun ErrorHandling(uiState: AuthUiState) {

    when (uiState) {
        is AuthUiState.Error -> {
            var errorMessage = (uiState as AuthUiState.Error).message
            when (errorMessage) {
                "The email address is badly formatted." -> errorMessage =
                    "L'adresse email est mal formatée"

                "The supplied auth credential is incorrect, malformed or has expired." -> errorMessage =
                    "Le mot de passe ou email est incorrect"

                "The given password is invalid. [ Password should be at least 6 characters ]" -> errorMessage =
                    "Le mot de passe doit contenir" +
                            " au moins 6 caractères"

                "The email address is already in use by another account." -> errorMessage =
                    "L'adresse email est déjà utilisée par un autre compte"

            }
            Text(errorMessage, textAlign = TextAlign.Center, color = Color(0xFFF37474))
        }


        AuthUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.width(24.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

        }

        else -> {}
    }
}
