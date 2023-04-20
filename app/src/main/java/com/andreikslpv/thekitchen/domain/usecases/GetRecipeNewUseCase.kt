package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.flow.Flow

class GetRecipeNewUseCase(
    private val recipeRepository: RecipeRepository,
) {
    suspend fun execute(): Flow<Response<List<RecipePreview>>> {
        return recipeRepository.getRecipeNew(listOf("re00000", "re01001", "re02001"))
    }
}