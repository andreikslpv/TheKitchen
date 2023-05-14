package com.andreikslpv.thekitchen.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andreikslpv.thekitchen.data.datasource.RecipeFavoritesDataSource
import com.andreikslpv.thekitchen.data.datasource.RecipePreviewDataSource
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipeDetails
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val PAGE_SIZE = 5

class RecipeRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : RecipeRepository {

    override suspend fun getRecipeNew(history: List<String>) = flow {
        try {
            val historyMutable = mutableListOf("re00000")
            historyMutable.addAll(history)
            emit(Response.Loading)
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)
            val result = collection
                .whereNotIn("id", historyMutable)
                .limit(5L)
                .get()
                .await()

            val tempList = result.documents.mapNotNull {
                it.toObject(RecipePreview::class.java)
            }

            emit(Response.Success(tempList))
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    override suspend fun getRecipeHistory(history: List<String>)= flow {
        try {
            emit(Response.Loading)
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)
            val result = collection
                .whereIn("id", history)
                .get()
                .await()

            val tempList = result.documents.mapNotNull {
                it.toObject(RecipePreview::class.java)
            }
            //создаем сопоставление списка, по которому хотим выполнить сортировку
            val idValueMap = tempList.associateBy { it.id }
            //повторяем список, по которому хотим выполнить сортировку,
            // и сопоставлям его значения объекту, который извлекли из только что созданного сопоставления
            val sortedList = history.map { idValueMap[it] }

            emit(Response.Success(sortedList.mapNotNull { it }))
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    override suspend fun getRecipePreview(filters: FiltersSeparated): Flow<PagingData<RecipePreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                RecipePreviewDataSource(
                    database,
                    filters,
                )
            }
        ).flow
    }

    override suspend fun getRecipeFavorites(favorites: List<String>): Flow<PagingData<RecipePreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                RecipeFavoritesDataSource(
                    database,
                    favorites,
                )
            }
        ).flow
    }

    override suspend fun getRecipeDetails(recipeId: String) = flow {
        val collection = database.collection(FirestoreConstants.PATH_RECIPE_DETAILS)
        val result = collection
            .whereEqualTo("id", recipeId)
            .limit(1L)
            .get()
            .await()

        val tempList = result.documents.mapNotNull {
            it.toObject(RecipeDetails::class.java)
        }
        if (tempList.isNotEmpty())
            emit(tempList[0])
    }

}