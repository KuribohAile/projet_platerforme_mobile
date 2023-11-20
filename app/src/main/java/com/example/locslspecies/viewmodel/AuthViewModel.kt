package com.example.locslspecies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locslspecies.model.AuthUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {
    val uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)
    //val uiState: StateFlow<AuthUiState> = _uiState
    val db = Firebase.firestore
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoggedIn.value = firebaseAuth.currentUser != null
    }


// inititialisation de l'authentification listener
    init {
        Firebase.auth.addAuthStateListener(authStateListener)
    }

  // fonction qui permet de s'inscrire
    fun register(email: String, password: String) {

        uiState.value = AuthUiState.Loading
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uiState.value = AuthUiState.Success


                } else {

                    uiState.value = AuthUiState.Error(
                        task.exception?.message ?: "Erreur inconnue"
                    )

                }
            }
    }

    // fonction qui permet de se connecter
    fun login(email: String, password: String) {
        uiState.value = AuthUiState.Loading // Indicate loading state
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    uiState.value = AuthUiState.Success // Indicate success
                } else {
                    uiState.value = AuthUiState.Error(
                        task.exception?.message ?: "Erreur inconnue"
                    ) // Indicate error state
                }
            }

    }

    // fonction qui permet de se deconnecter
    fun logout() {
        Firebase.auth.signOut()
    }

    // fonction qui permet de reinitialiser le mot de passe
    fun resetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
    }

    // fonction qui permet d'enregistrer les informations de l'utilisateur dans la base de données firestore
    fun registerUserInfos(user: User){
        // Add a new document with a generated ID
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("MY-TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("MY-TAG", "Error adding document", e)
            }

    }

    // fonction qui permet d'enregistrer le commentaire de l'utilisateur dans la base de données firestore pas encore fini
    fun commentOnImage(imageId: String, commentText: String) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val newComment = mapOf(
            "text" to commentText,
            "commentedBy" to userId,
            "commentedAt" to FieldValue.serverTimestamp()
        )

    }
    // fonction qui permet d'enregistrer le like de l'utilisateur dans la base de données firestore pas encore fini
    fun likeImage(imageId: String) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        // Firestore transaction to increment like count and add userId to likedBy
    }

    // fonction qui permet de supprimer le listener
    override fun onCleared() {
        super.onCleared()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }
}

