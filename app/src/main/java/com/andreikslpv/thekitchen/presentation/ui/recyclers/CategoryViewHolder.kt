package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemCategoryBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.bumptech.glide.Glide

class CategoryViewHolder(val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category, position: Int, type: CategoryType) {
        binding.itemTitle.text = category.name
        Glide.with(itemView)
            .load(getBackgroundByPositionAndType(position, type))
            .centerCrop()
            .into(binding.itemBackground)
        Glide.with(itemView)
            .load(category.image)
            .centerCrop()
            .into(binding.itemImage)
    }

    private fun getBackgroundByPositionAndType(position: Int, type: CategoryType): Drawable? {
        return when (type) {
            CategoryType.TIME -> getBackgroundByPositionForTime(position)
            CategoryType.DISH -> getBackgroundByPositionForDish(position)
            else -> null
        }
    }

    private fun getBackgroundByPositionForDish(position: Int): Drawable? {
        return when (position) {
            0, 6 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_category_1_7,
                null
            )
            1, 3, 5 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_category_2_4_6,
                null
            )
            2, 4, 7 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_category_3_5_8,
                null
            )
            else -> null
        }
    }

    private fun getBackgroundByPositionForTime(position: Int): Drawable? {
        return when (position) {
            0 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_time_1,
                null
            )
            1 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_time_2,
                null
            )
            2 -> ResourcesCompat.getDrawable(
                binding.root.context.resources,
                R.drawable.ic_background_time_3,
                null
            )
            else -> null
        }
    }
}