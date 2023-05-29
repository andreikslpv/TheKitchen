package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.Unit
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    fun updateLocalData(path: String)

    suspend fun getProductByIdFlow(productId: String): Flow<Product>

    suspend fun getProductById(productId: String): Product

    suspend fun getUnitById(unitId: String): Flow<Unit>

    suspend fun getAllUnits(): Flow<List<Unit>>
}