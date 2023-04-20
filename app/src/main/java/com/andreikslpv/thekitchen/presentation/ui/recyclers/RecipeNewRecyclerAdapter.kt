package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemRecipeNewBinding
import com.andreikslpv.thekitchen.domain.models.RecipePreview

class RecipeNewRecyclerAdapter(
    private val recipeItemClickListener: RecipeItemClickListener,
    private val favoriteClickListener: ItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<RecipePreview>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeNewViewHolder(
            ItemRecipeNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipeNewViewHolder -> {
                holder.bind(items[position])
                holder.binding.itemContainer.setOnClickListener {
                    recipeItemClickListener.click(items[position])
                }
                holder.binding.itemButtonFavorites.setOnClickListener {
                    favoriteClickListener.click(items[position].id)
                }
            }
        }
    }

    fun changeItems(list: List<RecipePreview>) {
        val diff = ItemDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}