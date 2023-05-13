package com.andreikslpv.thekitchen.domain

import androidx.paging.PagingData
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.RecipeDetails
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.Unit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RecipeRepository {

    suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    suspend fun getAllCategories(): MutableStateFlow<List<Category>>

    fun updateLocalData(path: String)

    suspend fun getRecipeNew(history: List<String>): Flow<Response<List<RecipePreview>>>

    suspend fun getRecipeHistory(history: List<String>): Flow<Response<List<RecipePreview>>>

    suspend fun getRecipePreview(filters: FiltersSeparated): Flow<PagingData<RecipePreview>>

    suspend fun getRecipeFavorites(favorites: List<String>): Flow<PagingData<RecipePreview>>

    suspend fun getRecipeDetails(recipeId: String): Flow<RecipeDetails>

    suspend fun getProductById(productId: String): Flow<Product>

    suspend fun getUnitById(unitId: String): Flow<Unit>

}