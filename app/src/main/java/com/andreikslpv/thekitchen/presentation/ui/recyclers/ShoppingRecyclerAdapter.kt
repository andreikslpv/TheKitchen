package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemShoppingBinding
import com.andreikslpv.thekitchen.domain.models.Ingredient

class ShoppingRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val items = mutableListOf<Ingredient>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShoppingViewHolder(
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShoppingViewHolder -> {
                val isLastItem = (position == itemCount - 1)
                holder.bind(items[position], isLastItem)
            }
        }
    }

    fun changeItems(list: List<Ingredient>) {
        val diff = ItemDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}