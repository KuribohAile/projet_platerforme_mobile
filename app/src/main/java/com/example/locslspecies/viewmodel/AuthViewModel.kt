package com.example.locslspecies.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locslspecies.ApiService
import com.example.locslspecies.model.ApiResponse
import com.example.locslspecies.model.Comments
import com.example.locslspecies.model.Coordinate
import com.example.locslspecies.model.Pictures
import com.example.locslspecies.model._User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class AuthViewModel : ViewModel() {
    val uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)
    val db = Firebase.firestore
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn
    val  userId = MutableLiveData<String>()
    private val storageRef = Firebase.storage.reference
    val pictures = MutableLiveData<List<Pictures>>()
    val users = MutableLiveData<List<_User>>()
    val userDocumentRef = MutableLiveData<DocumentReference>()
    val comments = MutableLiveData<List<Comments>>()
    val apiService = ApiService.create()
    var response = MutableLiveData<ApiResponse>()


    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoggedIn.value = firebaseAuth.currentUser != null
         userId.value = firebaseAuth.currentUser?.uid ?: ""

        if(userId.value !== ""){
         userDocumentRef.value = db.collection("Users").document(userId.value!!)
         fetchUser()
         fetchImages()
         fetchComments()
         fetchUsers()
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
        user.id = userId.value!!
        db.collection("Users")
            .add(user)
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

    fun fetchImages() {
        db.collection("Pictures").orderBy("postedAt", Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result!!.documents
                    val picturesList = mutableListOf<Pictures>()

                    documents.forEach { document ->
                        val picture = document.toObject(Pictures::class.java)
                        picturesList.add(picture!!)

                    }
                    pictures.value = picturesList
                }

            }
    }

    private fun fetchUser() {
        db.collection("Users").document(userId.value!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(_User::class.java)

                } else {

                }
            }
            .addOnFailureListener { exception ->
            }
    }


    fun fetchUsers() {
        db.collection("Users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result!!.documents
                    val userList = mutableListOf<_User>()

                    documents.forEach { document ->
                        val user = document.toObject(_User::class.java)
                        userList.add(user!!)

                    }
                    users.postValue(userList)
                }

            }
    }



    // fonction qui permet d'enregistrer le commentaire de l'utilisateur dans la base de données firestore pas encore fini
    fun addComment(comment: Comments, position: String) {

        db.collection("Comments").add(comment)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }


    fun fetchComments() {
        db.collection("Comments").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result!!.documents
                val commentsList = mutableListOf<Comments>()


                documents.forEach { document ->
                    val comment = document.toObject(Comments::class.java)
                    if (comment != null) {
                        commentsList.add(comment)
                    }
                }
                comments.postValue(commentsList)
            }
        }
    }

    fun pictureRecognition(capturedImageUri: Uri) {
        uploadImage(capturedImageUri, onSuccess = {
            viewModelScope.launch {
               response.value = apiService.getImageInfos(it.toString())
                Log.d("RESPP", "pictureRecognitions: + $it" + response.value!!.Name)
                val picture = Pictures(
                    id = UUID.randomUUID().toString(),
                    idUser = userId.value!!.toString(),
                    postedAt = Timestamp(Date()),
                    url = it.toString(),
                    scientificName = response.value!!.Scientific_name,
                    commonName = response.value!!.Name,
                    family = response.value!!.Family,
                    coordinate = listOf(Coordinate.latitude, Coordinate.longitude),
                    validation = 2,
                )
                insertUserPicture(picture = picture)

            }

        }, onFailure = {
    })
}

    fun PictureRecognitionBasedOnComments(){

        db.collection("Pictures")
            .get()
            .addOnSuccessListener { pictures ->
                pictures.forEach { pictureDocument ->
                    val picture = pictureDocument.toObject(Pictures::class.java)
                    val pictureId = picture.id

                    // Step 2: Fetch comments for each picture
                    db.collection("Comments")
                        .whereEqualTo("idPicture", pictureId)
                        .get()
                        .addOnSuccessListener { comments ->
                            // Step 3: Analyze comments for common words
                            val wordFrequency = HashMap<String, Int>()
                            comments.forEach { commentDocument ->
                                val comment = commentDocument.toObject(Comments::class.java)
                                comment.text.split("\\s+".toRegex()).forEach { word ->

                                    val cleanWord = word.lowercase().filter { it.isLetter() }

                                    wordFrequency[cleanWord] = wordFrequency.getOrDefault(cleanWord, 0) + 1

                                }
                            }

                            // Find the most common word that appears at least 3 times
                            val commonWord = wordFrequency.entries.find { it.value >= 3 }?.key


                            // Step 4: Update `commonName` if a common word is found
                            if (commonWord != null) {
                                Log.d("TAGJ", "PictureRecognitionBasedOnComments: ${ commonWord}")
                                db.collection("Pictures").whereEqualTo("id", pictureId).get()
                                    .addOnSuccessListener {documents ->

                                        for (document in documents) {
                                            // Update the field in each document
                                            document.getReference().update("commonName", commonWord)
                                                .addOnSuccessListener {

                                                }
                                                .addOnFailureListener { e ->
                                                }
                                        }

                                    }
                                    .addOnFailureListener { e ->

                                    }
                            }
                        }
                        .addOnFailureListener { e ->

                        }
                }
            }
            .addOnFailureListener { e ->

            }

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

