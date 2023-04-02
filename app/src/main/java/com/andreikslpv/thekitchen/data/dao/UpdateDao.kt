package com.andreikslpv.thekitchen.data.dao

import androidx.room.*
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.CategoryLocal
import com.andreikslpv.thekitchen.data.models.CategoryTypeLocal
import com.andreikslpv.thekitchen.data.models.ProductLocal
import com.andreikslpv.thekitchen.data.models.UnitLocal

@Dao
abstract class UpdateDao {

    @Query("DELETE FROM ${RoomConstants.TABLE_CACHED_CATEGORY_TYPE}")
    abstract fun deleteCategoryTypes(): Int

    @Insert(entity = CategoryTypeLocal::class, onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategoryTypes(types: List<CategoryTypeLocal>): List<Long>

    @Transaction
    open fun updateCategoryTypes(types: List<CategoryTypeLocal>) {
        deleteCategoryTypes()
        insertCategoryTypes(types)
    }


    @Query("DELETE FROM ${RoomConstants.TABLE_CACHED_CATEGORY}")
    abstract fun deleteCategories(): Int

    @Insert(entity = CategoryLocal::class, onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategories(categories: List<CategoryLocal>): List<Long>

    @Transaction
    open fun updateCategories(categories: List<CategoryLocal>) {
        deleteCategories()
        insertCategories(categories)
    }


    @Query("DELETE FROM ${RoomConstants.TABLE_CACHED_UNIT}")
    abstract fun deleteUnits(): Int

    @Insert(entity = UnitLocal::class, onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUnits(units: List<UnitLocal>): List<Long>

    @Transaction
    open fun updateUnits(units: List<UnitLocal>) {
        deleteUnits()
        insertUnits(units)
    }


    @Query("DELETE FROM ${RoomConstants.TABLE_CACHED_PRODUCT}")
    abstract fun deleteProducts(): Int

    @Insert(entity = ProductLocal::class, onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProducts(products: List<ProductLocal>): List<Long>

    @Transaction
    open fun updateProducts(products: List<ProductLocal>) {
        deleteProducts()
        insertProducts(products)
    }
}