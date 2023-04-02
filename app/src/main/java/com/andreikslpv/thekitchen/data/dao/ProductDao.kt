package com.andreikslpv.thekitchen.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.ProductLocal

@Dao
interface ProductDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_PRODUCT}")
    suspend fun getAllProducts(): List<ProductLocal>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_PRODUCT} WHERE ${RoomConstants.COLUMN_PRODUCT_ID} = :id")
    suspend fun getProductById(id: String): ProductLocal
}