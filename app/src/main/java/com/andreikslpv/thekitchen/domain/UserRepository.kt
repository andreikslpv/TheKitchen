package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Ingredient
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    suspend fun createUser(user: User): Flow<Response<Boolean>>

    suspend fun getCurrentUser(uid: String): Flow<User>

    fun getFavorites(): MutableStateFlow<List<String>>

    fun addToFavorites(uid: String, recipeId: String)

    fun removeFromFavorites(uid: String, recipeId: String)

    fun removeAllFromFavorites(uid: String)

    fun getHistory(): MutableStateFlow<List<String>>

    fun setHistory(uid: String, newHistory: List<String>)

    fun getDefaultExclude(): MutableStateFlow<List<String>>

    fun addToDefaultExclude(uid: String, categoryId: String)

    fun removeFromDefaultExclude(uid: String, categoryId: String)

    fun setDefaultExclude(uid: String, newExclude: List<String>)

    fun getShoppingList(): MutableStateFlow<List<Ingredient>>

    suspend fun setShoppingList(uid: String, newShoppingList: List<Ingredient>)

}