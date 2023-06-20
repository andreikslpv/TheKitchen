package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemCategoryBinding
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryRecyclerAdapter(
    private val categoryClickListener: ItemClickListener,
    private val type: CategoryType,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val animDuration = 300L
    private val animScale = 0.8f

    private val items = mutableListOf<Category>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.bind(items[position], position, type)
                holder.binding.itemContainer.setOnClickListener {
                    holder.binding.itemImage.animate()
                        .setDuration(animDuration)
                        .scaleX(animScale)
                        .scaleY(animScale)
                        .start()
                    CoroutineScope(Dispatchers.IO).launch { delay(100L) }
                    categoryClickListener.click(items[position].id)
                }
            }
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