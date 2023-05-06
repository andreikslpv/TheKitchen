package com.andreikslpv.thekitchen.presentation.ui.base

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.utils.visible
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

open class BaseRecipeViewHolder(open val binding: ItemRecipePreviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    open fun bind(recipe: RecipePreview) {
        binding.itemTitle.text = recipe.name
        binding.itemWarning.visible(recipe.isContainExclude)
        Glide.with(itemView)
            .load(recipe.imagePreview)
            .centerCrop()
            .into(binding.itemImage)
        binding.itemTimerValue.text = binding.root.context.getString(R.string.time, recipe.time)
        val kKal = recipe.caloriesCount.toDouble()
        val portionCount = recipe.portions
        val result = (kKal / portionCount).roundToInt()
        binding.itemKkalValue.text = binding.root.context.getString(R.string.kkal, result)
    }
}