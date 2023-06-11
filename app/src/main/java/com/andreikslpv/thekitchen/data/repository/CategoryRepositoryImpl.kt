package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.dao.CategoryDao
import com.andreikslpv.thekitchen.data.dao.UpdateDao
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Filters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    private val database: FirebaseFirestore,
    private val categoryDao: CategoryDao,
    private val updateDao: UpdateDao,
) : CategoryRepository {

    private val currentCategoryList = MutableStateFlow(emptyList<Category>())

    private val filters = MutableStateFlow(Filters())

    override fun updateCategories() {
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

    override suspend fun getAllCategories() = currentCategoryList

    override fun getCategoryById(id: String): Category? {
        currentCategoryList.value.forEach {
            if (it.id == id) return it
        }
        return null
    }

//    override suspend fun getCategoriesByType(type: String) = flow {
//        try {
//            emit(Response.Loading)
//            categoryDao.getCategoriesByType(type).collect {
//                emit(Response.Success(Mappers.LocalToCategoryListMapper.map(it)))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
//        }
//    }

//    override suspend fun getAllCategories() = flow {
//        try {
//            emit(Response.Loading)
//            categoryDao.getAllCategories().collect {
//                emit(Response.Success(Mappers.LocalToCategoryListMapper.map(it)))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
//        }
//    }

    override suspend fun setFilters(newFilters: Filters) {
        filters.value.addCategories(newFilters.getCategoriesList())
    }

    override suspend fun setExcludeFilters(newFilters: List<String>) {
        val exclude = getCategoriesIdByType(CategoryType.EXCLUDE.value)
        filters.value.removeCategories(exclude)
        newFilters.forEach {
            filters.value.addCategory(it)
        }
    }

    override suspend fun setFilterCategory(categoryId: String, type: CategoryType) {
        val dish = getCategoriesIdByType(type.value)
        filters.value.removeCategories(dish)
        filters.value.addCategory(categoryId)
    }

    override fun getCategoriesIdByType(type: String): List<String> {
        return currentCategoryList.value.filter { it.type == type }.map { it.id }
    }

    override suspend fun removeFilter(id: String) {
        val temp = filters.value
        temp.removeCategory(id)
        println("AAA removeFilter ${temp.getCategoriesList()}")
        filters.emit(temp)
    }

    override fun getFilters() = filters

}