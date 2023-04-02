package com.andreikslpv.thekitchen.domain.models

data class RecipePreview(
    val id: String = "re00000",
    val name: String = "recipe_preview",
    val categories: ArrayList<String> = arrayListOf(),
    val time: Int = 0,
    val timeUnit: String = "un00001",
    val caloriesCount: Int = 0,
    val caloriesUnit: String = "un00002",
    val portions: Int = 0,
    val imagePreview: String = "",
)