package com.andreikslpv.thekitchen.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.CategoryLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_CATEGORY} WHERE ${RoomConstants.COLUMN_CATEGORY_ID} <> \"ca00000\"")
    fun getAllCategories(): Flow<List<CategoryLocal>>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_CATEGORY} WHERE ${RoomConstants.COLUMN_CATEGORY_TYPE} = :typeId")
    fun getCategoriesByType(typeId: String): Flow<List<CategoryLocal>>

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_CATEGORY} WHERE ${RoomConstants.COLUMN_CATEGORY_ID} = :id")
    fun getCategoriyById(id: String): List<CategoryLocal>

}