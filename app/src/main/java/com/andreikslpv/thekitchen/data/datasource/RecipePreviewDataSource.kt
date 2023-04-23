package com.andreikslpv.thekitchen.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipePreviewDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val filters: Filters,
) : PagingSource<String, RecipePreview>() {
    private val defaultPage = RecipePreview().id
    private val step = defaultPage
    private val pageSize = 5
    private val defaultOrderingBy = "id"

    override fun getRefreshKey(state: PagingState<String, RecipePreview>) = defaultPage

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RecipePreview> {
        try {
            val pageNumber = params.key ?: step
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)
            val query = if (filters.getCategories().isNotEmpty())
                getQueryWithCategories(collection)
            else getQueryWithoutCategories(collection)

            val result = query
                .orderBy(defaultOrderingBy)
                .startAfter(pageNumber)
                .limit(pageSize.toLong())
                .get()
                .await()

            val tempList = result.documents.mapNotNull {
                it.toObject(RecipePreview::class.java)
            }

            return LoadResult.Page(
                data = tempList,
                prevKey = null,
                nextKey = if (tempList.size >= pageSize) tempList.last().id else null
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun getQueryWithCategories(collection: CollectionReference) =
        collection.whereNotEqualTo(defaultOrderingBy, defaultPage)
            .whereArrayContains("categories", filters.getCategories())

    private fun getQueryWithoutCategories(collection: CollectionReference) =
        collection.whereNotEqualTo(defaultOrderingBy, defaultPage)

    private fun getQueryWithSearch(query: Query) = query.whereEqualTo("name", filters.query)

}