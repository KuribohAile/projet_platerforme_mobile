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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.locslspecies.R
import com.example.locslspecies._ui.navigation.Route
import com.example.locslspecies.helper.ErrorHandling
import com.example.locslspecies.viewmodel.AuthViewModel

// fonction qui permet de créer la page de connexion de l'utilisateur
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = viewModel()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003654))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Local Species", color = Color.White, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(48.dp))
        Text("Se connecter", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextFieldSignIn(
            value = email.value,
            onValueChange = { newText -> email.value = newText },
            label = "Votre email"
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextFieldSignIn(
            value = password.value,
            onValueChange = { newText -> password.value = newText },
            label = "Votre mot de passe"

        )
        Spacer(modifier = Modifier.height(40.dp))
        Row {
            Button(onClick = { navController.navigate(Route.SignUp.screen_route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B808B), // Use primary color for the button background
                    contentColor = Color.White)
            ) {
                Text("S'inscrire", color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {

                if(email.value == "" || password.value == "") {
                    Toast.makeText(
                        navController.context,
                        "Veuillez remplir tous les champs svp",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    viewModel.login(email.value, password.value)
                    if (isLoggedIn){
                        navController.navigate(Route.Home.screen_route)
                    }
                }
           },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B808B), // Use primary color for the button background
                    contentColor = Color.White)
            ) {
                Text("Connexion", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        ErrorHandling(uiState)
        Spacer(modifier = Modifier.height(40.dp))

        Text("Mot de passe oublie", color = Color.White)
        Spacer(modifier = Modifier.height(40.dp))

        Text("Se connecter via", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        IconButton(onClick = { /* Handle Google Sign-In here */ }) {
            Icon(
                painter = painterResource(id = R.drawable.google_sign_in),
                contentDescription = "Google Sign-In"
            )
        }

    }
}

// customisation de l'input text
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldSignIn(
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


