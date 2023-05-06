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
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel : ViewModel() {

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var getRecipePreviewUseCase: GetRecipePreviewUseCase

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    val recipes: Flow<PagingData<RecipePreview>>

    private val _filters = MutableLiveData(Filters())
    val filters: LiveData<Filters> = _filters

    val categories = liveData {
        recipeRepository.getAllCategories().collect { response ->
            emit(response)
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

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect{}
        }

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

    fun tryToChangeFavoritesStatus(recipeId: String): Boolean {
        return tryToChangeFavoritesStatusUseCase.execute(recipeId)
    }

}