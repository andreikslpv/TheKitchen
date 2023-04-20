package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.Mappers
import com.andreikslpv.thekitchen.data.dao.CategoryDao
import com.andreikslpv.thekitchen.data.dao.UpdateDao
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryTypeDB
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val updateDao: UpdateDao,
    private val categoryDao: CategoryDao,
) : RecipeRepository {

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

    override fun updateLocalData(path: String) {
        when (path) {
            FirestoreConstants.PATH_CATEGORY_TYPE -> updateCategoryTypes()
            FirestoreConstants.PATH_CATEGORY -> updateCategories()
            else -> {}
        }
    }

    private fun updateCategoryTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_CATEGORY_TYPE)
            val result = collection.get().await()
            val tempList = result.documents.mapNotNull {
                it.toObject(CategoryTypeDB::class.java)
            }
            updateDao.updateCategoryTypes(Mappers.CategoryTypeToLocalListMapper.map(tempList))
        }
    }

    private fun updateCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = database.collection(FirestoreConstants.PATH_CATEGORY)
            val result = collection.get().await()
            val tempList = result.documents.mapNotNull {
                it.toObject(Category::class.java)
            }
            updateDao.updateCategories(Mappers.CategoryToLocalListMapper.map(tempList))
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

    override fun setFavoriteStatus(user: String, id: String, status: Boolean) {
        TODO("Not yet implemented")
    }


}