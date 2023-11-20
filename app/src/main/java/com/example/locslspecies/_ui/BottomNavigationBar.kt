package com.example.locslspecies._ui

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.locslspecies.R
import com.example.locslspecies.model.MyPlant
import com.example.locslspecies.viewmodel.AuthViewModel

// classe qui permet de definir les items de la bottom navigation bar
sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Home", R.drawable.home_24,"home")
    object Map: BottomNavItem("Map", R.drawable.map_24,"map")
    object Camera: BottomNavItem("Camera", R.drawable.camera_24,"camera")
    object Gallery: BottomNavItem("Gallerie", R.drawable.gallery_24,"gallerie")
    object Profile: BottomNavItem("Profile", R.drawable.profile_24,"profile")
    object SignIn: BottomNavItem("SignIn", R.drawable.home_24,"signIn")
    object SignUp: BottomNavItem("SignUp", R.drawable.home_24,"signUp")
}

// fonction qui permet d'implementer les items de la bottom navigation bar
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    AllUserslants: List<Plant>,
    Userplants: List<MyPlant>
) {
    val viewModel: AuthViewModel = viewModel()
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)


        // on definit les routes de la navigation
        NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
            composable("signIn") {
                SignInScreen(navController)
            }
            composable("signUp") {
                SignUpScreen(navController)
            }

            composable("home") {
                MyPlantList(Userplants)
            }
            composable(BottomNavItem.Home.screen_route) {
                // HomeScreen should only be accessible if the user is logged in
                if (isLoggedIn) {
                    MyPlantList(plantList = Userplants) // Replace with your actual home screen composable
                } else if(!isLoggedIn) {
                    SignInScreen(navController) // Redirect back to sign-in if not logged in
                }
            }

        composable(BottomNavItem.Map.screen_route) {
            MapViewComposable()
        }
        composable(BottomNavItem.Camera.screen_route) {
            CameraScreen()
        }

        composable(BottomNavItem.Gallery.screen_route) {
            PlantGalleryScreen(AllUserslants)
        }

        composable(BottomNavItem.Profile.screen_route) {
            ProfilePage(onDisconnect = {
                viewModel.logout()
                navController.navigate(BottomNavItem.SignIn.screen_route)
            })
        }

    }
}

// fonction qui permet d'implementer la bottom navigation bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Map,
        BottomNavItem.Camera,
        BottomNavItem.Gallery,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF3C757E),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                label = {
                    Text(text = screen.title!!)
                },
                icon = { Icon(painterResource(id = screen.icon), contentDescription = screen.title,
                    tint = Color(0xFFFFFFFF)
                )},
                selected = currentRoute == screen.screen_route,
                onClick = {
                    navController.navigate(screen.screen_route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray, selectedTextColor = Color.White,
                    indicatorColor = Color(0xFFB6B2B0)
                ),
            )
        }
    }

}