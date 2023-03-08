package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants.ERROR_MESSAGE
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val auth: FirebaseAuth
) {
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
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    fun getCurrentUser() = auth.currentUser
}