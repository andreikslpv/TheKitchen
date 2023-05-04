package com.andreikslpv.thekitchen.domain

import androidx.paging.PagingData
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RecipeRepository {

    suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    suspend fun getAllCategories(): MutableStateFlow<List<Category>>

    fun updateLocalData(path: String)

    suspend fun getRecipeNew(favorites: List<String>): Flow<Response<List<RecipePreview>>>

    suspend fun getRecipePreview(filters: FiltersSeparated): Flow<PagingData<RecipePreview>>
}