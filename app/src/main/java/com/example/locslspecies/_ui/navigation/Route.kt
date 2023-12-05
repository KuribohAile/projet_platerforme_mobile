package com.example.locslspecies._ui.navigation

import com.example.locslspecies.R


// Cette classe scellée représente les différents éléments de navigation dans l'application.
// Chaque objet représente une route avec un titre, une icône et un chemin d'écran associé.
sealed class Route(var title: String, var icon: Int, var screen_route: String) {

    object Home : Route("Accueil", R.drawable.home_24, "home")
    object Map : Route("Map", R.drawable.map_24, "map")
    object Camera : Route("Camera", R.drawable.camera_24, "camera")
    object Gallery : Route("Gallerie", R.drawable.gallery_24, "gallery")
    object Profile : Route("Profile", R.drawable.profile_24, "profile")
    object SignIn : Route("Connecter", R.drawable.home_24, "signIn")
    object SignUp : Route("Inscrire", R.drawable.home_24, "signUp")
    object Detail : Route("Detail", R.drawable.home_24, "detail/{idPicture}")

}
