package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.User
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : UserRepository {

    override suspend fun createUser(user: User) = flow {
        try {
            emit(Response.Loading)
            database.collection(FirestoreConstants.PATH_USERS).document(user.uid).set(user).await()
                .also {
                    emit(Response.Success(true))
                }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    override suspend fun getCurrentUser(uid: String) = callbackFlow {
        val favorites = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            .addSnapshotListener { value, error ->
                if (error == null && value != null)
                    trySend(value.toObject(User::class.java)!!)
            }
        awaitClose {
            favorites.remove()
        }
    }

    override fun addToFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update("favorites", FieldValue.arrayUnion(recipeId))
    }

    override fun removeFromFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update("favorites", FieldValue.arrayRemove(recipeId))
    }


}