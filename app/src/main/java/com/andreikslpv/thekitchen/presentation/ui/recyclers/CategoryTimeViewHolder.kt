package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemCategoryTimeBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.bumptech.glide.Glide

class CategoryTimeViewHolder(val binding: ItemCategoryTimeBinding) : RecyclerView.ViewHolder(binding.root) {

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