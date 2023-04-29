package com.andreikslpv.thekitchen.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val PAGE_SIZE = 5

class RecipePreviewDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val filters: FiltersSeparated,
) : PagingSource<Int, RecipePreview>() {
    private val defaultPage = 0

    override fun getRefreshKey(state: PagingState<Int, RecipePreview>): Int = defaultPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipePreview> {
        try {
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)

            val query = collection
                .whereArrayContainsAny("categoriesDish", filters.categoriesDish)
                .whereLessThanOrEqualTo("time", filters.categoriesTime)

            val result = query
                .get()
                .await()

            val tempList = result.documents
                .mapNotNull { it.toObject(RecipePreview::class.java) }
                .filter { it.id != RecipePreview().id }

            //println("AAA load: $tempList")

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


//    private fun Query.getQueryWithExclude(): Query {
//        val filter = arrayListOf<Filter>()
//        // отфильтровываем категории ограничений
//        val temp = filters.categoriesExclude.filter {
//            it.type == CategoryType.EXCLUDE.value
//        }
//        // если категории ограничений есть, то добавляем их в фильтр И
//        if (temp.isNotEmpty()) {
//            temp.forEach {
//                filter.add(equalTo(it.id, true))
//            }
//            this.where(and(*filter.toTypedArray()))
//        }
//        return this
//    }

}