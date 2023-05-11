package com.andreikslpv.thekitchen.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andreikslpv.thekitchen.data.Mappers
import com.andreikslpv.thekitchen.data.dao.CategoryDao
import com.andreikslpv.thekitchen.data.dao.ProductDao
import com.andreikslpv.thekitchen.data.dao.UnitDao
import com.andreikslpv.thekitchen.data.dao.UpdateDao
import com.andreikslpv.thekitchen.data.datasource.RecipeFavoritesDataSource
import com.andreikslpv.thekitchen.data.datasource.RecipePreviewDataSource
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.RecipeDetails
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.domain.models.Unit
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val PAGE_SIZE = 5

class RecipeRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val updateDao: UpdateDao,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val unitDao: UnitDao,
) : RecipeRepository {

    private val currentCategoryList = MutableStateFlow(emptyList<Category>())

    override suspend fun getCategoriesByType(type: String) = flow {
        try {
            emit(Response.Loading)
            categoryDao.getCategoriesByType(type).collect {
                emit(Response.Success(Mappers.LocalToCategoryListMapper.map(it)))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    override suspend fun getAllCategories() = currentCategoryList

    override fun updateLocalData(path: String) {
        when (path) {
            FirestoreConstants.PATH_PRODUCT -> updateProducts()
            FirestoreConstants.PATH_UNIT -> updateUnits()
            FirestoreConstants.PATH_CATEGORY -> updateCategories()
            else -> {}
        }
    }

    private fun updateProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_PRODUCT)
            val result = collection.get().await()
            val tempList = result.documents.mapNotNull {
                it.toObject(Product::class.java)
            }
            updateDao.updateProducts(Mappers.ProductToLocalListMapper.map(tempList))
        }
    }

    private fun updateUnits() {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_UNIT)
            val result = collection.get().await()
            val tempList = result.documents.mapNotNull {
                it.toObject(Unit::class.java)
            }
            updateDao.updateUnits(Mappers.UnitToLocalListMapper.map(tempList))
        }
    }

    private fun updateCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_CATEGORY)
            val result = collection.get().await()
            val tempList = result.documents.mapNotNull {
                it.toObject(Category::class.java)
            }
            currentCategoryList.emit(tempList)
            //updateDao.updateCategories(Mappers.CategoryToLocalListMapper.map(tempList))
        }
    }

    override suspend fun getRecipeNew(favorites: List<String>) = flow {
        try {
            emit(Response.Loading)
            val collection = database.collection(FirestoreConstants.PATH_RECIPE_PREVIEW)
            val result = collection
                .whereNotIn("id", favorites)
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

    override suspend fun getProductById(productId: String) = productDao.getProductById(productId)
        .map {
            Mappers.LocalToProductMapper.map(it)
        }


    override suspend fun getUnitById(unitId: String) = unitDao.getUnitById(unitId)
        .map {
            Mappers.LocalToUnitMapper.map(it)
        }

}