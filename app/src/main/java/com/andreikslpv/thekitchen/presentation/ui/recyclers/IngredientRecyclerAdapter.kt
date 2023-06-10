package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemIngredientBinding
import com.andreikslpv.thekitchen.domain.models.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow

class IngredientRecyclerAdapter(private val ratio: MutableStateFlow<Double>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Ingredient>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IngredientViewHolder(
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientViewHolder -> {
                holder.bind(items[position], ratio)
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