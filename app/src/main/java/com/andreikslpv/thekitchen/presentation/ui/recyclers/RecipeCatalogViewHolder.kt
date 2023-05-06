package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.databinding.ItemRecipePreviewBinding
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import com.andreikslpv.thekitchen.presentation.ui.base.BaseRecipeViewHolder
import com.andreikslpv.thekitchen.presentation.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeCatalogViewHolder(override val binding: ItemRecipePreviewBinding) :
    BaseRecipeViewHolder(binding) {

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    override fun bind(recipe: RecipePreview) {
        super.bind(recipe)
        CoroutineScope(Dispatchers.Main).launch {
            userRepository.getFavorites().collect {
                if (it.contains(recipe.id))
                    binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites_fill)
                else
                    binding.itemButtonFavorites.setImageResource(R.drawable.ic_favorites)
            }
        }
        if (recipe.isContainExclude)
            binding.itemWarning.visible(true)
        else
            binding.itemWarning.visible(false)
    }
}