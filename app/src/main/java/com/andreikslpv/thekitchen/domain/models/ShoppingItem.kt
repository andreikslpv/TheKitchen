package com.andreikslpv.thekitchen.domain.models

data class ShoppingItem(
    val id: String = "si00000",
    val showingName: String = "",
    val note: String = "",
    val ingredient: Ingredient = Ingredient(),
)
