package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetRecipePreviewUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
import com.andreikslpv.thekitchen.domain.usecases.SetHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveAllFromFavoritesUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveFromFavoritesUseCase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideInitApplicationSettingsUseCase(
        settingsRepository: SettingsRepository,
        recipeRepository: RecipeRepository,
        remoteConfig: FirebaseRemoteConfig
    ): InitApplicationSettingsUseCase {
        return InitApplicationSettingsUseCase(settingsRepository, recipeRepository, remoteConfig)
    }

    @Provides
    @Singleton
    fun provideGetRecipeNewUseCase(
        recipeRepository: RecipeRepository,
    ): GetRecipeNewUseCase {
        return GetRecipeNewUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipePreviewUseCase(
        recipeRepository: RecipeRepository,
    ): GetRecipePreviewUseCase {
        return GetRecipePreviewUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserFromDbUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): GetUserFromDbUseCase {
        return GetUserFromDbUseCase(userRepository, authRepository)
    }

    @Provides
    @Singleton
    fun provideTryToChangeFavoritesStatusUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): TryToChangeFavoritesStatusUseCase {
        return TryToChangeFavoritesStatusUseCase(userRepository, authRepository)
    }

    @Provides
    @Singleton
    fun provideTryToRemoveFromFavoritesUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): TryToRemoveFromFavoritesUseCase {
        return TryToRemoveFromFavoritesUseCase(userRepository, authRepository)
    }

    @Provides
    @Singleton
    fun provideTryToRemoveAllFromFavoritesUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): TryToRemoveAllFromFavoritesUseCase {
        return TryToRemoveAllFromFavoritesUseCase(userRepository, authRepository)
    }

    @Provides
    @Singleton
    fun provideSetHistoryUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): SetHistoryUseCase {
        return SetHistoryUseCase(userRepository, authRepository)
    }

}