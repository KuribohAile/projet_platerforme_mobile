package com.example.locslspecies._ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.locslspecies._ui.CameraScreen
import com.example.locslspecies._ui.MapViewScreen
import com.example.locslspecies._ui.HomeScreen
import com.example.locslspecies._ui.GalleryScreen
import com.example.locslspecies._ui.ProfileScreen
import com.example.locslspecies._ui.SignInScreen
import com.example.locslspecies._ui.SignUpScreen
import com.example.locslspecies.model.UserPictures
import com.example.locslspecies.model.UsersPictures
import com.example.locslspecies.viewmodel.AuthViewModel

// Ce NavHost gère la navigation dans l'application. Il définit les routes et les écrans correspondants.
// Les utilisateurs non connectés sont redirigés vers l'écran de connexion.
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraph(
    navController: NavHostController,
    userPictures: List<UserPictures>,
    usersPictures: List<UsersPictures>
) {
    val viewModel: AuthViewModel = viewModel()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)

    NavHost(navController, startDestination = Route.Home.screen_route) {
        composable("signIn") {
            SignInScreen(navController)
        }
        composable("signUp") {
            SignUpScreen(navController)
        }

        composable("home") {
            HomeScreen(usersPictures)
        }
        composable(Route.Home.screen_route) {

            if (isLoggedIn) {
                HomeScreen(usersPictures)
            } else if (!isLoggedIn) {
                SignInScreen(navController)
            }
        }

        composable(Route.Map.screen_route) {
            MapViewScreen()
        }
        composable(Route.Camera.screen_route) {
            CameraScreen()
        }

        composable(Route.Gallery.screen_route) {
            GalleryScreen(userPictures)
        }

        composable(Route.Profile.screen_route) {
            ProfileScreen(onDisconnect = {
                viewModel.logout()
                navController.navigate(Route.SignIn.screen_route)
            })
        }

    }
}
