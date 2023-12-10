package com.example.locslspecies.controller

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import java.util.Date
import java.util.UUID

// Classe ViewModel pour gérer l'authentification et les interactions avec la base de données Firestore.
class AuthViewModel : ViewModel() {
    // État de l'interface utilisateur pour l'authentification.
    val uiState = MutableStateFlow<AuthUiState>(AuthUiState.Empty)

    // Référence à la base de données Firestore.
    val db = Firebase.firestore

    // État pour savoir si l'utilisateur est connecté.
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    // ID de l'utilisateur actuellement connecté.
    val userId = MutableLiveData<String>()

    // Référence au stockage Firebase pour les images.
    private val storageRef = Firebase.storage.reference

    // Données des images, utilisateurs et commentaires.
    val pictures = MutableLiveData<List<Pictures>>()
    val users = MutableLiveData<List<_User>>()
    val userDocumentRef = MutableLiveData<DocumentReference>()
    val comments = MutableLiveData<List<Comments>>()


    // Écouteur de l'état d'authentification Firebase.
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoggedIn.value = firebaseAuth.currentUser != null
         userId.value = firebaseAuth.currentUser?.uid ?: ""
// Si l'utilisateur est connecté, récupérer les données nécessaires.
        if(userId.value !== ""){
         userDocumentRef.value = db.collection("Users").document(userId.value!!)
         fetchImages()
         fetchComments()
         fetchUsers()
        }


    }

    // Initialisation de l'écouteur d'authentification.
    init { Firebase.auth.addAuthStateListener(authStateListener) }


    // Fonction pour l'inscription d'un nouvel utilisateur.
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

    // Fonction pour la connexion d'un utilisateur.
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

    // Fonction pour la déconnexion d'un utilisateur.
    fun logout() {
        Firebase.auth.signOut()
    }


    // Fonction pour enregistrer les informations de l'utilisateur dans Firestore.
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

    // Fonction pour insérer une image capturée par l'utilisateur.
    fun insertUserPicture(capturedImageUri: Uri){
        uploadImage(capturedImageUri, onSuccess = {

            val picture = Pictures(
                id = UUID.randomUUID().toString(),
                idUser = userId.value!!.toString(),
                postedAt = Timestamp(Date()),
                url = it.toString(),
                scientificName = "Inconnu",
                commonName = "Inconnu",
                family = "Inconnu",
                coordinate = listOf(Coordinate.latitude, Coordinate.longitude),
                validation = 0,
            )
            db.collection("Pictures").add(picture)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                }

        }, onFailure = {})

    }

    // Fonction pour télécharger une image sur Firebase Storage.
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

    // Fonction pour récupérer les images depuis Firestore.
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

    // Fonction pour récupérer les utilisateurs depuis Firestore.
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



    // Fonction pour ajouter un commentaire dans Firestore.
    fun addComment(comment: Comments, position: String) {

        db.collection("Comments").add(comment)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }

    // Fonction pour récupérer les commentaires depuis Firestore.
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

    // Fonction pour analyser les commentaires et reconnaître les espèces de plantes en se basant sur les commentaires.
    fun PictureRecognitionBasedOnComments(){

        db.collection("Pictures")
            .get()
            .addOnSuccessListener { pictures ->
                pictures.forEach { pictureDocument ->
                    val picture = pictureDocument.toObject(Pictures::class.java)
                    val pictureId = picture.id


                    db.collection("Comments")
                        .whereEqualTo("idPicture", pictureId)
                        .get()
                        .addOnSuccessListener { comments ->

                            val wordFrequency = HashMap<String, Int>()
                            comments.forEach { commentDocument ->
                                val comment = commentDocument.toObject(Comments::class.java)
                                comment.text.split("\\s+".toRegex()).forEach { word ->

                                    val cleanWord = word.lowercase().filter { it.isLetter() }

                                    wordFrequency[cleanWord] = wordFrequency.getOrDefault(cleanWord, 0) + 1

                                }
                            }


                            val commonWord = wordFrequency.entries.find { it.value >= 3 }?.key



                            if (commonWord != null) {
                                Log.d("TAGJ", "PictureRecognitionBasedOnComments: ${ commonWord}")
                                db.collection("Pictures").whereEqualTo("id", pictureId).get()
                                    .addOnSuccessListener {documents ->

                                        for (document in documents) {

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

    // Fonction pour incrémenter le champ de validation d'une image.
    fun incrementValidationField(pictureFieldId: String) {

        db.collection("Pictures")
            .whereEqualTo("id", pictureFieldId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("Firestore", "No document found with field ID: $pictureFieldId")
                    return@addOnSuccessListener
                }


                val documentRef = documents.documents.first().reference


                db.runTransaction { transaction ->
                    val snapshot = transaction.get(documentRef)
                    val currentValidation = snapshot.getLong("validation") ?: 0
                    transaction.update(documentRef, "validation", currentValidation + 1)
                    fetchImages()
                }.addOnSuccessListener {

                }.addOnFailureListener { e ->

                }
            }
            .addOnFailureListener { e ->

            }
    }


    // fonction qui permet de supprimer le listener
    override fun onCleared() {
        super.onCleared()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }
}

