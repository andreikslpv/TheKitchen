package com.andreikslpv.thekitchen.domain.models

data class ShoppingItem(
    val showingName: String = "",
    val note: String = "",
    val ingredient: Ingredient = Ingredient(),
)