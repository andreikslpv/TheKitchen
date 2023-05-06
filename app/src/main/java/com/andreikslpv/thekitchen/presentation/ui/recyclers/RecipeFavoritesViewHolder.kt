package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseRecipeViewHolder

class RecipeFavoritesViewHolder(override val binding: ItemRecipePreviewBinding) :
    BaseRecipeViewHolder(binding) {

    override fun bind(recipe: RecipePreview) {
        super.bind(recipe)
        binding.itemButtonFavorites.setImageResource(R.drawable.ic_close_line)
    }
}