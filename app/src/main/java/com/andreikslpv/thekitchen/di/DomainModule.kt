package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetRecipePreviewUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
import com.andreikslpv.thekitchen.domain.usecases.SetDefaultExcludeFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.SetHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeExcludeStatusUseCase
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
        ingredientRepository: IngredientRepository,
        categoryRepository: CategoryRepository,
        remoteConfig: FirebaseRemoteConfig,
    ): InitApplicationSettingsUseCase {
        return InitApplicationSettingsUseCase(
            settingsRepository,
            ingredientRepository,
            categoryRepository,
            remoteConfig
        )
    }

    @Provides
    @Singleton
    fun provideGetRecipeNewUseCase(
        userRepository: UserRepository,
        recipeRepository: RecipeRepository,
    ): GetRecipeNewUseCase {
        return GetRecipeNewUseCase(userRepository, recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeHistoryUseCase(
        userRepository: UserRepository,
        recipeRepository: RecipeRepository,
    ): GetRecipeHistoryUseCase {
        return GetRecipeHistoryUseCase(userRepository, recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipePreviewUseCase(
        recipeRepository: RecipeRepository,
        categoryRepository: CategoryRepository,
    ): GetRecipePreviewUseCase {
        return GetRecipePreviewUseCase(recipeRepository, categoryRepository)
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
    fun provideTryToChangeExcludeStatusUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
    ): TryToChangeExcludeStatusUseCase {
        return TryToChangeExcludeStatusUseCase(userRepository, authRepository)
    }

    @Provides
    @Singleton
    fun provideSetDefaultExcludeFromDbUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository,
        categoryRepository: CategoryRepository,
    ): SetDefaultExcludeFromDbUseCase {
        return SetDefaultExcludeFromDbUseCase(userRepository, authRepository, categoryRepository)
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