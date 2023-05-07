package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.User
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : UserRepository {

   private val favorites = MutableStateFlow(emptyList<String>())
   private val history = MutableStateFlow(emptyList<String>())

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
                if (error == null && value != null) {
                    val user = value.toObject(User::class.java)!!
                    trySend(user)
                    println("AAA getCurrentUser $user")
                    favorites.tryEmit(user.favorites)
                    history.tryEmit(user.history)
                }
            }
        awaitClose {
            favorites.remove()
        }
    }

    override fun getFavorites(): MutableStateFlow<List<String>> {
        return favorites
    }

    override fun addToFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update("favorites", FieldValue.arrayUnion(recipeId))
    }

    override fun removeFromFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update("favorites", FieldValue.arrayRemove(recipeId))
    }

    override fun removeAllFromFavorites(uid: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update("favorites", arrayListOf<String>())
    }

    override fun getHistory(): MutableStateFlow<List<String>> {
        return history
    }


}