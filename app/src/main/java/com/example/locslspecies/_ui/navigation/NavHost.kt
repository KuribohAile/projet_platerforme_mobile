package com.example.locslspecies._ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.locslspecies.model.UserPictures
import com.example.locslspecies.model.UsersPictures


/**
 * Composant 'NavHost' qui définit la navigation de l'application.
 * Gère le routage entre les différents écrans, utilisant les listes 'userPictures' et 'usersPictures' pour passer des données aux écrans.
 */
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavHost(
    navController: NavHostController,
    userPictures: List<UserPictures>,

) {

    NavGraph(navController, userPictures)

}
