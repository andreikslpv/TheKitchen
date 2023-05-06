package com.andreikslpv.thekitchen.domain

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

}