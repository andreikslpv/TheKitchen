package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemCategoryBinding
import com.andreikslpv.thekitchen.databinding.ItemCategoryTimeBinding
import com.andreikslpv.thekitchen.domain.models.Category


const val ITEM_TIME = 1
const val ITEM_DISH = 2

class CategoryRecyclerAdapter(
    private val categoryClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Category>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TIME -> CategoryTimeViewHolder(
                ItemCategoryTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ITEM_DISH -> CategoryDishViewHolder(
                ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> CategoryDishViewHolder(
                ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryDishViewHolder -> {
                holder.bind(items[position])
                holder.binding.itemContainer.setOnClickListener {
                    categoryClickListener.click(items[position].id)
                }
            }
            is CategoryTimeViewHolder -> {
                holder.bind(items[position])
                holder.binding.itemContainer.setOnClickListener {
                    categoryClickListener.click(items[position].id)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            "ct00001" -> ITEM_TIME
            "ct00002" -> ITEM_DISH
            else -> 0
        }
    }

    fun changeItems(list: List<Category>) {
        val diff = ItemDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}