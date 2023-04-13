package com.andreikslpv.thekitchen.presentation.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemStoryBinding

class StoriesRecyclerAdapter(
    private val categoryClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items =
        listOf(R.drawable.card_story_1, R.drawable.card_story_2, R.drawable.card_story_3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StoriesViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StoriesViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount() = items.size
}