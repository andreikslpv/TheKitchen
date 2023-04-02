package com.andreikslpv.thekitchen.di

import android.content.Context
import androidx.room.Room
import com.andreikslpv.thekitchen.data.db.AppDatabase
import com.andreikslpv.thekitchen.data.db.RoomConstants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule() {

//    @Provides
//    fun provideContext() = context

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            RoomConstants.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideCategoryTypeDao(appDatabase: AppDatabase) = appDatabase.categoryTypeDao()

    @Singleton
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideUnitDao(appDatabase: AppDatabase) = appDatabase.unitDao()

    @Singleton
    @Provides
    fun provideProductDao(appDatabase: AppDatabase) = appDatabase.productDao()

    @Singleton
    @Provides
    fun provideUpdateDao(appDatabase: AppDatabase) = appDatabase.updateDao()
}