package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseRecipePreviewViewHolder
import com.andreikslpv.thekitchen.presentation.ui.models.RecipePreviewType

class RecipePreviewPagingAdapter(
    private val recipeItemClickListener: RecipeItemClickListener,
    private val favoriteClickListener: ItemClickListener,
    private val type: RecipePreviewType,
) : PagingDataAdapter<RecipePreview, BaseRecipePreviewViewHolder>(RecipePreviewItemDiff()) {

    override fun onBindViewHolder(holder: BaseRecipePreviewViewHolder, position: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecipePreviewViewHolder {
        return when (type) {
            RecipePreviewType.CATALOG -> RecipeCatalogPreviewViewHolder(
                ItemRecipePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            RecipePreviewType.FAVORITES -> RecipeFavoritesPreviewViewHolder(
                ItemRecipePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    }
}