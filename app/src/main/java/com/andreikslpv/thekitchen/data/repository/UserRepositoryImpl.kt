package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
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
   private val defaultExclude = MutableStateFlow(emptyList<String>())
   private val shoppingList = MutableStateFlow(emptyList<ShoppingItem>())

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
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
            .addSnapshotListener { value, error ->
                if (error == null && value != null) {
                    val user = value.toObject(User::class.java) ?: User()
                    trySend(user)
                    favorites.tryEmit(user.favorites)
                    history.tryEmit(user.history)
                    defaultExclude.tryEmit(user.defaultExclude)
                    shoppingList.tryEmit(user.shoppingList)
                }
            }
        awaitClose {
            user.remove()
        }
    }

    override fun getFavorites(): MutableStateFlow<List<String>> {
        return favorites
    }

    override fun addToFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_FAVORITES, FieldValue.arrayUnion(recipeId))
    }

    override fun removeFromFavorites(uid: String, recipeId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_FAVORITES, FieldValue.arrayRemove(recipeId))
    }

    override fun removeAllFromFavorites(uid: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_FAVORITES, arrayListOf<String>())
    }

    override fun getHistory(): MutableStateFlow<List<String>> {
        return history
    }

    override fun setHistory(uid: String, newHistory: List<String>) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_HISTORY, newHistory)
    }

    override fun getDefaultExclude(): MutableStateFlow<List<String>> {
        return defaultExclude
    }

    override fun addToDefaultExclude(uid: String, categoryId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_DEFAULT_EXCLUDE, FieldValue.arrayUnion(categoryId))
    }

    override fun removeFromDefaultExclude(uid: String, categoryId: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_DEFAULT_EXCLUDE, FieldValue.arrayRemove(categoryId))
    }

    override fun setDefaultExclude(uid: String, newExclude: List<String>) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_DEFAULT_EXCLUDE, newExclude)
    }

    override fun getShoppingList(): MutableStateFlow<List<ShoppingItem>> {
        return shoppingList
    }

    override suspend fun setShoppingList(uid: String, newShoppingList: List<ShoppingItem>) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_SHOPPING_LIST, newShoppingList)
    }

    override fun addToShoppingList(uid: String, shoppingItem: ShoppingItem) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_SHOPPING_LIST, FieldValue.arrayUnion(shoppingItem))
    }

    override fun removeFromShoppingList(uid: String, shoppingItem: ShoppingItem) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_SHOPPING_LIST, FieldValue.arrayRemove(shoppingItem))
    }

    override fun removeAllFromShoppingList(uid: String) {
        val user = database.collection(FirestoreConstants.PATH_USERS).document(uid)
        user.update(FirestoreConstants.FIELD_SHOPPING_LIST, arrayListOf<ShoppingItem>())
    }

}