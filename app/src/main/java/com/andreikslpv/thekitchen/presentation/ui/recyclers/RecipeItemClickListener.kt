package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.domain.models.RecipePreview

interface RecipeItemClickListener {
    fun click(recipePreview: RecipePreview)
}