package com.andreikslpv.thekitchen.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.CategoryTypeLocal

@Dao
interface CategoryTypeDao {

    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_CATEGORY_TYPE}")
    suspend fun getAllCategoryTypes(): List<CategoryTypeLocal>
}