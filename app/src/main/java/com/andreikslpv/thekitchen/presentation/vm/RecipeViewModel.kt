package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import com.andreikslpv.thekitchen.domain.models.RecipeDetails
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.domain.usecases.SetHistoryUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToAddIngredientToShoppingListUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModel : ViewModel() {

    private val maxPortionsCount = 9
    private val minPortionsCount = 1
    private val defaultRatio = 1.0

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    @Inject
    lateinit var setHistoryUseCase: SetHistoryUseCase

    @Inject
    lateinit var tryToAddIngredientToShoppingListUseCase: TryToAddIngredientToShoppingListUseCase

    private val _recipePreview = MutableLiveData(RecipePreview())
    val recipePreview: LiveData<RecipePreview> = _recipePreview

    val recipeDetails: LiveData<RecipeDetails>

    private val _portions = MutableLiveData(0)
    val portions: LiveData<Int> = _portions

    val ingredients = MutableLiveData(listOf<Ingredient>())

    val ratio = MutableStateFlow(defaultRatio)

    init {
        App.instance.dagger.inject(this)

        recipeDetails = _recipePreview
            .asFlow()
            .flatMapLatest {
                recipeRepository.getRecipeDetails(it.id)
            }.asLiveData(EmptyCoroutineContext, 5000L)
    }

    fun setIngredients(newIngredients: List<Ingredient>) {
        ingredients.value = newIngredients
    }

    fun changeIngredients(newPortionsCount: Int) {
        if (recipeDetails.isInitialized && _recipePreview.isInitialized) {
            val oldPortionsCount = _recipePreview.value!!.portions
            CoroutineScope(Dispatchers.Main).launch {
                if (newPortionsCount == oldPortionsCount) ratio.emit(defaultRatio)
                else ratio.emit(newPortionsCount.toDouble() / oldPortionsCount)
            }
        }
    }

    fun setRecipePreview(recipe: RecipePreview) {
        _recipePreview.value = recipe
        setHistoryUseCase.execute(recipe.id)
    }

    fun validAndSetPortionsCount(newCount: Int) {
        if (newCount in minPortionsCount..maxPortionsCount)
            _portions.value = newCount
    }

    fun upPortionsCount() {
        val temp = _portions.value?.plus(1) ?: 0
        validAndSetPortionsCount(temp)
    }

    fun downPortionsCount() {
        val temp = _portions.value?.minus(1) ?: 0
        validAndSetPortionsCount(temp)
    }

    fun tryToChangeFavoritesStatus(): Boolean {
        return if (recipePreview.value != null)
            tryToChangeFavoritesStatusUseCase.execute(recipePreview.value!!.id)
        else false
    }

    fun tryToAddIngredientToShoppingList(): Boolean? {
        return ingredients.value?.let {
            tryToAddIngredientToShoppingListUseCase.execute(it, ratio.value)
        }
    }

}