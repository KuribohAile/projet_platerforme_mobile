package com.example.locslspecies._ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


/**
 * Composant 'BottomBar' pour Jetpack Compose.
 * Cette fonction crée une barre de navigation inférieure personnalisée qui permet de naviguer entre différents écrans de l'application.
 * Elle utilise un 'NavHostController' pour gérer les événements de navigation et affiche des éléments de navigation (comme 'Home', 'Map', 'Camera', etc.)
 * selon la configuration spécifiée. La barre adapte son affichage en fonction de la route actuelle, mettant en évidence l'élément de navigation sélectionné.
 */

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Route.Home,
        Route.Map,
        Route.Camera,
        Route.Gallery,
        Route.Profile
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
                icon = { Icon(
                    painterResource(id = screen.icon), contentDescription = screen.title,
                    tint = Color(0xFFFFFFFF)
                )
                },
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
                    indicatorColor = Color(0xCB472D3C)
                ),
            )
        }
    }

}