package com.example.locslspecies.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locslspecies.model.Pictures
import com.example.locslspecies.model._User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow


class AuthViewModel : ViewModel() {
    val uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)
    val db = Firebase.firestore
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn
    val  userId = MutableLiveData<String>()
    private val storageRef = Firebase.storage.reference
    val pictures = MutableLiveData<List<Pictures>>()
    val user = MutableLiveData<_User>()
    val userDocumentRef = MutableLiveData<DocumentReference>()


    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoggedIn.value = firebaseAuth.currentUser != null
         userId.value = firebaseAuth.currentUser?.uid ?: ""

        if(userId.value !== ""){
         userDocumentRef.value = db.collection("Users").document(userId.value!!)
         fetchUser()
         fetchImages()
        }


    }

// inititialisation de l'authentification listener
    init {

        Firebase.auth.addAuthStateListener(authStateListener)

}


  // fonction qui permet de s'inscrire
     fun register(user: _User) {

        uiState.value = AuthUiState.Loading
        Firebase.auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uiState.value = AuthUiState.Success
                    registerUserInfos(user)

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
    fun registerUserInfos(user: _User){
        // Add a new document with a generated ID
        db.collection("Users").document(userId.value!!)
            .set(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->

            }
    }

    fun insertUserPicture(picture: Pictures){

        db.collection("Pictures").add(picture)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->

            }
    }

    fun uploadImage(fileUri: Uri, onSuccess: (Uri) -> Unit, onFailure: (Exception) -> Unit) {
        val imageRef = storageRef.child("images/${fileUri.lastPathSegment}")
        val uploadTask = imageRef.putFile(fileUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                onSuccess(downloadUri)
            } else {
                task.exception?.let { onFailure(it) }
            }
        }
    }

    fun fetchImages(){
        var picturesList = listOf<Pictures>()
        db.collection("Pictures").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                 picturesList = task.result!!.map { document ->
                    val picture = document.toObject(Pictures::class.java)

                    fetchUserInfos(document["postedByRef"] as DocumentReference) { user ->
                        if (user != null) {

                            picture.postedBy = user
                        } else {
                            // Handle the error or absence of user data
                        }
                    }
                    picture
                }
                pictures.postValue(picturesList)
            } else {
                // Handle error
            }
        }
    }


    fun fetchUserInfos(userRef: DocumentReference, callback: (user: _User?) -> Unit) {
        db.collection("Users").document(userRef.id).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(_User::class.java)
                callback(user)
            }
            .addOnFailureListener {
                // Handle any errors here, for example, by calling the callback with null
                callback(null)
            }
    }

    private fun fetchUser() {
        db.collection("Users").document(userId.value!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(_User::class.java)


                    // Use the user object as needed
                } else {
                    // Handle the case where the document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
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

