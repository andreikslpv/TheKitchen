package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview

class RecipePreviewPagingAdapter(
    private val recipeItemClickListener: RecipeItemClickListener,
    private val favoriteClickListener: ItemClickListener,
) : PagingDataAdapter<RecipePreview, RecipePreviewViewHolder>(RecipePreviewItemDiff()) {

    override fun onBindViewHolder(holder: RecipePreviewViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

        holder.binding.itemContainer.setOnClickListener {
            getItem(position)?.let { recipe ->
                recipeItemClickListener.click(recipe)
            }
        }
        holder.binding.itemButtonFavorites.setOnClickListener {
            getItem(position)?.let { recipe ->
                favoriteClickListener.click(recipe.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipePreviewViewHolder {
        return RecipePreviewViewHolder(
            ItemRecipePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}