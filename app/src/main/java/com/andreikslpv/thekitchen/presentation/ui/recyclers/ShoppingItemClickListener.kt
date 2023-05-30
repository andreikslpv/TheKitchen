package com.andreikslpv.thekitchen.presentation.ui.recyclers

import com.andreikslpv.thekitchen.domain.models.ShoppingItem

interface ShoppingItemClickListener {
    fun click(shoppingItem: ShoppingItem)
}