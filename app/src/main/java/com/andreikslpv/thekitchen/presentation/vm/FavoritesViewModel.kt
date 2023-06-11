package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.usecases.ClearFiltersDishAndTimeUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveAllFromFavoritesUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToRemoveFromFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel : ViewModel() {

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var tryToRemoveFromFavoritesUseCase: TryToRemoveFromFavoritesUseCase

    @Inject
    lateinit var tryToRemoveAllFromFavoritesUseCase: TryToRemoveAllFromFavoritesUseCase

    @Inject
    lateinit var clearFiltersDishAndTimeUseCase: ClearFiltersDishAndTimeUseCase

    val recipes: Flow<PagingData<RecipePreview>>

    private val favorites = liveData(Dispatchers.IO) {
        userRepository.getFavorites().collect { response ->
            emit(response)
        }
    }

    init {
        App.instance.dagger.inject(this)

        recipes = favorites
            .asFlow()
            .flatMapLatest { recipeRepository.getRecipeFavorites(it) }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)

        // удаляем фильтры категорий времени и типа блюда, чтобы при переходе на экран Поиск их не было
        clearFiltersDishAndTimeUseCase.execute()
    }

    fun tryToRemoveFromFavorites(recipeId: String): Boolean {
        return tryToRemoveFromFavoritesUseCase.execute(recipeId)
    }

    fun tryToRemoveAllFromFavorites(): Boolean {
        return tryToRemoveAllFromFavoritesUseCase.execute()
    }

}