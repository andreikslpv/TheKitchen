package com.andreikslpv.thekitchen.domain.models

data class Ingredient(
    val product: String = "pr00000",
    val unit: String = "un00000",
    var count: Double = 0.0,
)