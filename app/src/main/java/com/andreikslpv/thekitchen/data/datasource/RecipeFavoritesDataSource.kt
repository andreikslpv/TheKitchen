package com.andreikslpv.thekitchen.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeFavoritesDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val favorites: List<String>,
) : PagingSource<Int, RecipePreview>() {
    private val defaultPage = 0

    override fun getRefreshKey(state: PagingState<Int, RecipePreview>): Int = defaultPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipePreview> {
        try {
            var tempList = listOf<RecipePreview>()
            if (favorites.isNotEmpty()) {
                val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)

                val result = collection
                    .whereIn("id", favorites)
                    .get()
                    .await()

                tempList = result.documents
                    .asSequence()
                    .mapNotNull { it.toObject(RecipePreview::class.java) }
                    .filter { it.id != RecipePreview().id }
                    .toList()
            }

            return LoadResult.Page(
                data = tempList,
                prevKey = null,
                nextKey = null
            )

        } catch (e: Exception) {
            println("AAA error: ${e.message}")
            return LoadResult.Error(e)
        }
    }

}