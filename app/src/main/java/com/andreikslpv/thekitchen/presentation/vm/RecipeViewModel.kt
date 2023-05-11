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
import com.andreikslpv.thekitchen.domain.usecases.GetUserFromDbUseCase
import com.andreikslpv.thekitchen.domain.usecases.TryToChangeFavoritesStatusUseCase
import com.andreikslpv.thekitchen.presentation.utils.roundTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModel : ViewModel() {

    private val maxPortionsCount = 9
    private val minPortionsCount = 1

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var getUserFromDbUseCase: GetUserFromDbUseCase

    @Inject
    lateinit var tryToChangeFavoritesStatusUseCase: TryToChangeFavoritesStatusUseCase

    private val _recipePreview = MutableLiveData(RecipePreview())
    val recipePreview: LiveData<RecipePreview> = _recipePreview

    val recipeDetails: LiveData<RecipeDetails>

    private val _portions = MutableLiveData(0)
    val portions: LiveData<Int> = _portions

    val ingredients = MutableLiveData(listOf<Ingredient>())

    val kkal = MutableLiveData(0)

    init {
        App.instance.dagger.inject(this)

        recipeDetails = _recipePreview
            .asFlow()
            .flatMapLatest {
                recipeRepository.getRecipeDetails(it.id)
            }.asLiveData(EmptyCoroutineContext, 5000L)

        // начинаем отслеживать данные пользователя в бд
        CoroutineScope(Dispatchers.IO).launch {
            getUserFromDbUseCase.execute().collect {}
        }
    }

    fun setKkal(newKkal: Int) {
        kkal.value = newKkal
    }

    fun changeKkal(newPortionsCount: Int) {
        if (_recipePreview.isInitialized) {
            val oldPortionsCount = _recipePreview.value!!.portions
            if (newPortionsCount == oldPortionsCount) {
                setKkal(_recipePreview.value!!.caloriesCount)
            } else {
                val ratio = newPortionsCount.toDouble() / oldPortionsCount
                setKkal((_recipePreview.value!!.caloriesCount * ratio).roundToInt())
            }
        }
    }

    fun setIngredients(newIngredients: List<Ingredient>) {
        ingredients.value = newIngredients
    }

    fun changeIngredients(newPortionsCount: Int) {
        if (recipeDetails.isInitialized && _recipePreview.isInitialized) {
            val oldPortionsCount = _recipePreview.value!!.portions
            if (newPortionsCount == oldPortionsCount) {
                setIngredients(recipeDetails.value!!.ingredients)
            } else {
                val newIngredients = arrayListOf<Ingredient>()
                val ratio = newPortionsCount.toDouble() / oldPortionsCount
                recipeDetails.value!!.ingredients.forEach {
                    newIngredients.add(
                        Ingredient(
                            product = it.product,
                            unit = it.unit,
                            count = (ratio * it.count).roundTo(1)
                        )
                    )
                }
                setIngredients(newIngredients)
            }
        }
    }

    fun setRecipePreview(recipe: RecipePreview) {
        _recipePreview.value = recipe
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


}