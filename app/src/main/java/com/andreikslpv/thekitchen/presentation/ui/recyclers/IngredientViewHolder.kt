package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemIngredientBinding
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun bind(ingredient: Ingredient) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getProductByIdFlow(ingredient.product).collect {
                withContext(Dispatchers.Main) {
                    binding.itemIngredientName.text = it.name
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getUnitById(ingredient.unit).collect {
                withContext(Dispatchers.Main) {
                    binding.itemIngredientCount.text = binding.root.context.getString(
                        R.string.ingredient_count,
                        ingredientCountToString(ingredient.count),
                        it.name
                    )
                }
            }
        }
    }

    private fun ingredientCountToString(count: Double): String {
        if (count == 0.0) return ""
        return if (count % 1 == 0.0) count.toInt().toString()
        else count.toString()
    }
}