package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    fun updateLocalData(path: String)

    suspend fun getRecipeNew(favorites: List<String>): Flow<Response<List<RecipePreview>>>

    fun setFavoriteStatus(user: String, id: String, status: Boolean)
}