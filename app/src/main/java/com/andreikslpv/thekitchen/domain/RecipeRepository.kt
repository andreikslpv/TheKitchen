package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    fun updateLocalData(path: String)
}