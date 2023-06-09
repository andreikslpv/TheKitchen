package com.andreikslpv.thekitchen.data.repository

import android.net.Uri
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants.ERROR_MESSAGE
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
) {

    val isUserAuthenticatedInFirebase get() = auth.currentUser != null

    suspend fun firebaseSignInWithGoogle(idToken: String?) = flow {
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
    fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    fun getCurrentUser() = auth.currentUser

    suspend fun firebaseDeleteUser(idToken: String?) = flow {
        try {
            emit(Response.Loading)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.currentUser?.reauthenticate(credential)?.await().also {
                auth.currentUser?.delete()!!.await().also {
                    emit(Response.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    suspend fun editUserName(newName: String) = flow {
        try {
            emit(Response.Loading)
            val profileUpdates = userProfileChangeRequest { displayName = newName }
            auth.currentUser?.updateProfile(profileUpdates)?.await().also {
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

    suspend fun changeAvatar(uri: Uri) = flow {
        try {
            emit(Response.Loading)
            val profileUpdates = userProfileChangeRequest { photoUri = uri }
            auth.currentUser?.updateProfile(profileUpdates)?.await().also {
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: ERROR_MESSAGE))
        }
    }

}