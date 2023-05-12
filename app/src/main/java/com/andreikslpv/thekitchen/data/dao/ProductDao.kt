package com.andreikslpv.thekitchen.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.ProductLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_PRODUCT}")
    fun getAllProducts(): Flow<List<ProductLocal>>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_PRODUCT} WHERE ${RoomConstants.COLUMN_PRODUCT_ID} = :id")
    fun getProductById(id: String): Flow<ProductLocal>
}