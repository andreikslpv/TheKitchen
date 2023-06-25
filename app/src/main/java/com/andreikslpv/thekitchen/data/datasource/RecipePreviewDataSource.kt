package com.andreikslpv.thekitchen.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class RecipePreviewDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val filters: FiltersSeparated,
) : PagingSource<Int, RecipePreview>() {
    private val defaultPage = 0

    override fun getRefreshKey(state: PagingState<Int, RecipePreview>): Int = defaultPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipePreview> {
        try {
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)

            val result = collection
                .whereArrayContainsAny("categoriesDish", filters.categoriesDish)
                .whereLessThanOrEqualTo("time", filters.timeLimit)
                .get()
                .await()

            val tempList = result.documents
                .asSequence()
                .mapNotNull { it.toObject(RecipePreview::class.java) }
                .filter { it.id != RecipePreview().id }
                .filter {
                    if (filters.query.isNotBlank())
                        it.name.lowercase(Locale.getDefault()).trim().contains(filters.query)
                    else true
                }
                .map {
                    if (filters.categoriesExclude.isNotEmpty())
                        if (!it.categoriesExclude.containsAll(filters.categoriesExclude))
                            it.isContainExclude = true
                    it
                }
                .sortedBy { it.isContainExclude }
                .toList()

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