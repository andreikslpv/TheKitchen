package com.andreikslpv.thekitchen.data.repository

import com.andreikslpv.thekitchen.data.Mappers
import com.andreikslpv.thekitchen.data.dao.ProductDao
import com.andreikslpv.thekitchen.data.dao.UnitDao
import com.andreikslpv.thekitchen.data.dao.UpdateDao
import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.Unit
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class IngredientRepositoryImpl(
    private val database: FirebaseFirestore,
    private val updateDao: UpdateDao,
    private val productDao: ProductDao,
    private val unitDao: UnitDao,
) : IngredientRepository {

    override fun updateLocalData(path: String) {
        when (path) {
            FirestoreConstants.PATH_PRODUCT -> updateProducts()
            FirestoreConstants.PATH_UNIT -> updateUnits()
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

    override suspend fun getProductByIdFlow(productId: String) =
        productDao.getProductByIdFlow(productId)
            .map { Mappers.LocalToProductMapper.map(it) }

    override suspend fun getProductById(productId: String) =
        Mappers.LocalToProductMapper.map(productDao.getProductById(productId))

    override suspend fun getUnitById(unitId: String) = unitDao.getUnitById(unitId)
        .map { Mappers.LocalToUnitMapper.map(it) }

    override suspend fun getAllUnits() = unitDao.getAllUnits()
        .map { Mappers.LocalToUnitListMapper.map(it) }

}