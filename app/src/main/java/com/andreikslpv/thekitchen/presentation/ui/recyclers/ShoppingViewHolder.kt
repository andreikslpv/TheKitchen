package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.graphics.Paint
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemShoppingBinding
import com.andreikslpv.thekitchen.domain.IngredientRepository
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import com.andreikslpv.thekitchen.presentation.utils.ingredientCountToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun bind(
        shoppingItem: ShoppingItem,
        isLastItem: Boolean,
        selectedShoppingItem: MutableStateFlow<MutableList<ShoppingItem>>
    ) {
        binding.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.itemProductName.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setTextColor(
                            binding.root.context.resources.getColor(
                                R.color.text_menu_select,
                                binding.root.context.theme
                            )
                        )
                    } else {
                        setTextColor(binding.root.context.resources.getColor(R.color.text_menu_select))
                    }
                }
            } else {
                binding.itemProductName.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setTextColor(
                            binding.root.context.resources.getColor(
                                R.color.text_card_title,
                                binding.root.context.theme
                            )
                        )
                    } else {
                        setTextColor(binding.root.context.resources.getColor(R.color.text_card_title))
                    }
                }
            }
        }

        binding.itemProductName.text = shoppingItem.showingName
        CoroutineScope(Dispatchers.IO).launch {
            ingredientRepository.getUnitByIdFlow(shoppingItem.ingredient.unit).collect {
                withContext(Dispatchers.Main) {
                    binding.itemProductCount.text = binding.root.context.getString(
                        R.string.ingredient_count,
                        shoppingItem.ingredient.count.ingredientCountToString(),
                        it.name
                    )
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            selectedShoppingItem.collect {
                withContext(Dispatchers.Main) {
                    binding.itemCheckBox.isChecked = it.contains(shoppingItem)
                }
            }
        }
        if (isLastItem) binding.itemDivider.visibility = View.INVISIBLE
    }

}