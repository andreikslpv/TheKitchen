package com.andreikslpv.thekitchen.domain.models

data class ShoppingItem(
    var showingName: String = "",
    val ingredient: Ingredient = Ingredient(),
)