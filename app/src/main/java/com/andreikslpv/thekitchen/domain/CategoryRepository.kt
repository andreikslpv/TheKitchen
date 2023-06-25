package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Filters
import kotlinx.coroutines.flow.MutableStateFlow

interface CategoryRepository {

    fun updateCategories()

    suspend fun getAllCategories(): MutableStateFlow<List<Category>>

    fun getCategoryById(id: String): Category?

    //suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    //suspend fun getAllCategories(): Flow<Response<List<Category>>>

    fun getFilters(): MutableStateFlow<Filters>

    fun clearFiltersDishAndTime()

    suspend fun setFilters(newFilters: Filters)

    suspend fun setExcludeFilters(newFilters: List<String>)

    suspend fun setFilterCategory(categoryId: String, type: CategoryType)

    fun getCategoriesIdByType(type: String): List<String>

    suspend fun removeFilter(id: String)

}