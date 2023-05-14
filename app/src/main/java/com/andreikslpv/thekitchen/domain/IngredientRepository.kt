package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.Unit
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    fun updateLocalData(path: String)

    suspend fun getProductById(productId: String): Flow<Product>

    suspend fun getUnitById(unitId: String): Flow<Unit>
}