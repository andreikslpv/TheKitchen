package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.domain.models.Ingredient

interface ShoppingItemClickListener {
    fun click(ingredient: Ingredient)
}