package com.andreikslpv.thekitchen.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreikslpv.thekitchen.data.dao.*
import com.andreikslpv.thekitchen.data.models.CategoryLocal
import com.andreikslpv.thekitchen.data.models.CategoryTypeLocal
import com.andreikslpv.thekitchen.data.models.ProductLocal
import com.andreikslpv.thekitchen.data.models.UnitLocal

@Database(
    entities = [CategoryTypeLocal::class, CategoryLocal::class, UnitLocal::class, ProductLocal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryTypeDao(): CategoryTypeDao

    abstract fun categoryDao(): CategoryDao

    abstract fun unitDao(): UnitDao

    abstract fun productDao(): ProductDao

    abstract fun updateDao(): UpdateDao
}