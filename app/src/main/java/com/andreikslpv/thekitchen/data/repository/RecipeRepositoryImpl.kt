package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.db.DbConstants
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : RecipeRepository {

    override suspend fun getAllCategories() = flow {
        try {
            emit(Response.Loading)
            val refCategory = database.getReference(DbConstants.PATH_CATEGORY)
            val categoryResult = refCategory.get().await()
            val tempList = mutableListOf<Category>()
            categoryResult.children.forEach { item ->
                item.getValue(Category::class.java)?.let { value ->
                    if (value.id != "ca00000")
                        tempList.add(value)
                }
            }
            emit(Response.Success(tempList.toList()))
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

    override fun updateLocalData(path: String) {

    }
}