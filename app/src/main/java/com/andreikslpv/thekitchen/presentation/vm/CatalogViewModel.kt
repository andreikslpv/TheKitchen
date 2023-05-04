package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.usecases.GetRecipePreviewUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel : ViewModel() {

    @Inject
    lateinit var repository: RecipeRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipePreviewUseCase: GetRecipePreviewUseCase

    val recipes: Flow<PagingData<RecipePreview>>

    private val _filters = MutableLiveData(Filters())
    val filters: LiveData<Filters> = _filters

    val categories = liveData {
        repository.getAllCategories().collect { response ->
            emit(response)
        }
    }

    val currentUserFromAuth by lazy {
        authRepository.getCurrentUser()
    }

    val currentUserFromDb = liveData(Dispatchers.IO) {
        authRepository.getCurrentUser()?.let {
            userRepository.getCurrentUser(it.uid).collect { response ->
                emit(response)
            }
        }
    }

    init {
        App.instance.dagger.inject(this)

        recipes = _filters
            .asFlow()
            .flatMapLatest {
                getRecipePreviewUseCase.execute(it)
            }
            // кешируем прлучившийся flow, чтобы на него можно было подписаться несколько раз
            .cachedIn(viewModelScope)
    }

    fun getCategoryById(id: String): Category? {
        categories.value?.forEach {
            if (it.id == id) return it
        }
        return null
    }

    fun addFilters(filtersArray: Array<String>?) {
        filtersArray?.let {
            _filters.value?.addCategories(it)
            refresh()
        }
    }

    fun removeFilter(category: String) {
        _filters.value?.removeCategory(category)
        refresh()
    }

    fun refresh() {
        _filters.postValue(_filters.value)
    }

    fun changeFavoritesStatus(recipeId: String) = authRepository.getCurrentUser()
        ?.let { user ->
            currentUserFromDb.value?.favorites?.let { favorites ->
                if (favorites.contains(recipeId))
                    userRepository.removeFromFavorites(user.uid, recipeId)
                else
                    userRepository.addToFavorites(user.uid, recipeId)
            }
        }

}