package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

class StoriesViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(@DrawableRes storyId: Int) {
        Glide.with(itemView)
            .load(storyId)
            .centerCrop()
            .into(binding.itemImage)
    }
}