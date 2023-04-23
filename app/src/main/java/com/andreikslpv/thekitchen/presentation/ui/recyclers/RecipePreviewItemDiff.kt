package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.DiffUtil
import com.andreikslpv.thekitchen.domain.models.RecipePreview

class RecipePreviewItemDiff: DiffUtil.ItemCallback<RecipePreview>() {
    override fun areItemsTheSame(oldItem: RecipePreview, newItem: RecipePreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipePreview, newItem: RecipePreview): Boolean {
        return oldItem == newItem
    }
}