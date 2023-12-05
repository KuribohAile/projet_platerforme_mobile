package com.example.locslspecies._ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.locslspecies._ui.screen.CameraScreen
import com.example.locslspecies._ui.screen.DetailScreen
import com.example.locslspecies._ui.screen.MapViewScreen
import com.example.locslspecies._ui.screen.HomeScreen
import com.example.locslspecies._ui.screen.GalleryScreen
import com.example.locslspecies._ui.screen.ProfileScreen
import com.example.locslspecies._ui.screen.SignInScreen
import com.example.locslspecies._ui.screen.SignUpScreen
import com.example.locslspecies.viewmodel.AuthViewModel

// Ce NavHost gère la navigation dans l'application. Il définit les routes et les écrans correspondants.
// Les utilisateurs non connectés sont redirigés vers l'écran de connexion.
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    val viewModel: AuthViewModel = viewModel()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState<Boolean>()


    NavHost(navController, startDestination = Route.Home.screen_route) {
        composable("signIn") {
            SignInScreen(navController)
        }
        composable("signUp") {
            SignUpScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }
        composable(Route.Home.screen_route) {

            if (isLoggedIn == true) {
                HomeScreen(navController)
            } else if (isLoggedIn == false) {
                SignInScreen(navController)
            }
        }

        composable(Route.Map.screen_route) {
            MapViewScreen(navController)
        }

        composable(Route.Camera.screen_route) {

            CameraScreen(navBackStackEntry = it, navController = navController)
        }

        composable(Route.Gallery.screen_route) {
            GalleryScreen(navBackStackEntry = it, navController)
        }

        composable(Route.Detail.screen_route, arguments = listOf(navArgument("idPicture") { type = NavType.StringType })) {
            DetailScreen(navBackStackEntry = it)
        }

        composable(Route.Profile.screen_route) {
            ProfileScreen(onDisconnect = {
                viewModel.logout()
                navController.navigate(Route.SignIn.screen_route)
            })
        }

    }
}
