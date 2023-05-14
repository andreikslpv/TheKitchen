package com.andreikslpv.thekitchen.domain

import androidx.paging.PagingData
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipeDetails
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipeNew(history: List<String>): Flow<Response<List<RecipePreview>>>

    suspend fun getRecipeHistory(history: List<String>): Flow<Response<List<RecipePreview>>>

    suspend fun getRecipePreview(filters: FiltersSeparated): Flow<PagingData<RecipePreview>>

    suspend fun getRecipeFavorites(favorites: List<String>): Flow<PagingData<RecipePreview>>

    suspend fun getRecipeDetails(recipeId: String): Flow<RecipeDetails>



}