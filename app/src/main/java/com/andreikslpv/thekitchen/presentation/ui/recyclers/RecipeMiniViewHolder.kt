package com.andreikslpv.thekitchen.presentation.ui.recyclers

import androidx.recyclerview.widget.RecyclerView
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipeNewBinding
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeMiniViewHolder(val binding: ItemRecipeNewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun bind(recipe: RecipePreview) {
        binding.itemTitle.text = recipe.name
        Glide.with(itemView)
            .load(recipe.imagePreview)
            .centerCrop()
            .into(binding.itemImage)
        CoroutineScope(Dispatchers.Main).launch {
            userRepository.getFavorites().collect {
                if (it.contains(recipe.id))
                    binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites_fill)
                else
                    binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites)
            }
        }
    }
}