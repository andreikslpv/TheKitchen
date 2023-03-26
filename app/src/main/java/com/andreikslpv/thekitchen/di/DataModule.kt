package com.andreikslpv.thekitchen.di

import android.content.Context
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.data.repository.RecipeRepositoryImpl
import com.andreikslpv.thekitchen.data.repository.SettingsRepositoryImpl
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    fun provideRecipeRepository(database: FirebaseDatabase): RecipeRepository {
        return RecipeRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

}