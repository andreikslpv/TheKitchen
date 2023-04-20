package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipeNewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.bumptech.glide.Glide

class RecipeNewViewHolder(val binding: ItemRecipeNewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: RecipePreview) {
        binding.itemTitle.text = recipe.name
        Glide.with(itemView)
            .load(recipe.imagePreview)
            .centerCrop()
            .into(binding.itemImage)
        if (recipe.isFavorite)
            binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites_fill)
        else
            binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites)
    }
}