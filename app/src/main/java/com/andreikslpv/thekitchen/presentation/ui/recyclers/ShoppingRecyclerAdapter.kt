package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.databinding.ItemShoppingBinding
import com.andreikslpv.thekitchen.domain.models.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow

class ShoppingRecyclerAdapter(
    private val editItemClickListener: ShoppingItemClickListener,
    private val selectItemClickListener: ShoppingItemClickListener,
    private val selectedShoppingItem: MutableStateFlow<MutableList<ShoppingItem>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val items = mutableListOf<ShoppingItem>()

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
                holder.bind(items[position], isLastItem, selectedShoppingItem)
                holder.binding.itemCheckBox.setOnClickListener {
                    selectItemClickListener.click(items[position])
                }
                holder.binding.itemPencil.setOnClickListener {
                    editItemClickListener.click(items[position])
                }
            }
        }
    }

    fun changeItems(list: List<ShoppingItem>) {
        val diff = ItemDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}