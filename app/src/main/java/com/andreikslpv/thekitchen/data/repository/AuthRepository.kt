package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants.ERROR_MESSAGE
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    //private val database: FirebaseDatabase
) {

    val isUserAuthenticatedInFirebase get() = auth.currentUser != null

    init {
        //database.setPersistenceEnabled(true)
    }

    suspend fun firebaseSignInWithGoogle(idToken: String) = flow {
        try {
            emit(Response.Loading)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.additionalUserInfo?.apply {
                emit(Response.Success(isNewUser))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    fun signOut() = flow {
        try {
            emit(Response.Loading)
            googleSignInClient.signOut().await().also {
                emit(Response.Success(it))
            }
            auth.signOut()
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    @ExperimentalCoroutinesApi
    fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    fun getCurrentUser() = auth.currentUser

//    suspend fun createUserInFirestore() = flow {
//        try {
//            emit(Response.Loading)
//            auth.currentUser?.apply {
//                val ref = database.getReference(DbConstants.PATH_USERS)
//                val subref = ref.child(uid)
//                subref.setValue(
//                    User(
//                        uid,
//                        displayName ?: "",
//                        email ?: "",
//                        photoUrl?.toString() ?: "",
//                        System.currentTimeMillis()
//                    )
//                ).await().also {
//                    emit(Response.Success(it))
//                }
//                val subref2 = ref.child("uid2")
//                subref2.setValue(
//                    User(
//                        uid,
//                        displayName ?: "",
//                        email ?: "",
//                        photoUrl?.toString() ?: "",
//                        System.currentTimeMillis()
//                    )
//                ).await().also {
//                    emit(Response.Success(it))
//                }
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
//        }
//    }
}