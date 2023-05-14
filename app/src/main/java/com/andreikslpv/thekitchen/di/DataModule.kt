package com.andreikslpv.thekitchen.di

import android.content.Context
import com.andreikslpv.thekitchen.data.dao.CategoryDao
import com.andreikslpv.thekitchen.data.dao.ProductDao
import com.andreikslpv.thekitchen.data.dao.UnitDao
import com.andreikslpv.thekitchen.data.dao.UpdateDao
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.data.repository.CategoryRepositoryImpl
import com.andreikslpv.thekitchen.data.repository.IngredientRepositoryImpl
import com.andreikslpv.thekitchen.data.repository.RecipeRepositoryImpl
import com.andreikslpv.thekitchen.data.repository.SettingsRepositoryImpl
import com.andreikslpv.thekitchen.data.repository.UserRepositoryImpl
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): AuthRepository {
        return AuthRepository(auth, googleSignInClient)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseFirestore,
    ): UserRepository {
        return UserRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        database: FirebaseFirestore,
    ): RecipeRepository {
        return RecipeRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideIngredientRepository(
        database: FirebaseFirestore,
        updateDao: UpdateDao,
        productDao: ProductDao,
        unitDao: UnitDao,
    ): IngredientRepository {
        return IngredientRepositoryImpl(database, updateDao, productDao, unitDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        database: FirebaseFirestore,
        categoryDao: CategoryDao,
        updateDao: UpdateDao,
    ): CategoryRepository {
        return CategoryRepositoryImpl(database, categoryDao, updateDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

}