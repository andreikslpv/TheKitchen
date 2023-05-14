package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Filters
import kotlinx.coroutines.flow.MutableStateFlow

interface CategoryRepository {

    fun updateCategories()

    suspend fun getAllCategories(): MutableStateFlow<List<Category>>

    fun getCategoryById(id: String): Category?

    //suspend fun getCategoriesByType(type: String): Flow<Response<List<Category>>>

    //suspend fun getAllCategories(): Flow<Response<List<Category>>>

    fun getFilters(): MutableStateFlow<Filters>

    suspend fun setFilters(newFilters: Filters)

    fun setFilterDish(categoryId: String)

    suspend fun removeFilter(id: String)

}