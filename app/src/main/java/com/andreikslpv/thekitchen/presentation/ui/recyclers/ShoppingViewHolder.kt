package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemShoppingBinding
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.Ingredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoppingViewHolder(val binding: ItemShoppingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var ingredientRepository: IngredientRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun bind(ingredient: Ingredient, isLastItem: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getProductById(ingredient.product).collect {
                withContext(Dispatchers.Main) {
                    binding.itemProductName.text = it.name
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getUnitById(ingredient.unit).collect {
                withContext(Dispatchers.Main) {
                    binding.itemProductCount.text = binding.root.context.getString(
                        R.string.ingredient_count,
                        ingredientCountToString(ingredient.count),
                        it.name
                    )
                }
            }
        }
        if (isLastItem) binding.itemDivider.visibility = View.INVISIBLE
    }

    private fun ingredientCountToString(count: Double): String {
        if (count == 0.0) return ""
        return if (count % 1 == 0.0) count.toInt().toString()
        else count.toString()
    }
}