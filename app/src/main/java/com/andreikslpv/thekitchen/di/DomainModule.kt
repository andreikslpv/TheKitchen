package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.usecases.GetRecipeNewUseCase
import com.andreikslpv.thekitchen.domain.usecases.GetRecipePreviewUseCase
import com.andreikslpv.thekitchen.domain.usecases.InitApplicationSettingsUseCase
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
        userRepository: UserRepository
    ): GetRecipePreviewUseCase {
        return GetRecipePreviewUseCase(recipeRepository, userRepository)
    }

}