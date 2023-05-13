package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class GetRecipeHistoryUseCase(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
) {
    suspend fun execute(): Flow<Response<List<RecipePreview>>> {
        val job = CoroutineScope(Dispatchers.IO).async {
            userRepository.getHistory().value
        }

        return recipeRepository.getRecipeHistory(job.await())
    }
}