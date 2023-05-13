package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseRecipePreviewViewHolder

class RecipeFavoritesPreviewViewHolder(override val binding: ItemRecipePreviewBinding) :
    BaseRecipePreviewViewHolder(binding) {

    override fun bind(recipe: RecipePreview) {
        super.bind(recipe)
        binding.itemButtonFavorites.setImageResource(R.drawable.ic_close_line)
    }
}