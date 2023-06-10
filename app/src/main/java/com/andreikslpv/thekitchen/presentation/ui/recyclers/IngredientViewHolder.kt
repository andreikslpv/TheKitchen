package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemIngredientBinding
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import com.andreikslpv.thekitchen.presentation.utils.ingredientCountToString
import com.andreikslpv.thekitchen.presentation.utils.roundTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IngredientViewHolder(val binding: ItemIngredientBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var ingredientRepository: IngredientRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun bind(ingredient: Ingredient, ratio: MutableStateFlow<Double>) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getProductByIdFlow(ingredient.product).collect {
                withContext(Dispatchers.Main) {
                    binding.itemIngredientName.text = it.name
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            ratio.collect {
                val count = (it * ingredient.count).roundTo(1).ingredientCountToString()
                binding.itemIngredientCount.text = count
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getUnitByIdFlow(ingredient.unit).collect {
                withContext(Dispatchers.Main) {
                    binding.itemIngredientUnit.text = binding.root.context.getString(
                        R.string.ingredient_count,
                        "",
                        it.name
                    )
                }
            }
        }
    }

}