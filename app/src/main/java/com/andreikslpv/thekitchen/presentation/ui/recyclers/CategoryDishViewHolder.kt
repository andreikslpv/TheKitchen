package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemCategoryBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.bumptech.glide.Glide

class CategoryDishViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category) {
        binding.itemTitle.text = category.name
        if (category.image.isNotBlank()) {
            Glide.with(itemView)
                .load(category.image)
                .centerCrop()
                .into(binding.itemImage)
        }
    }
}