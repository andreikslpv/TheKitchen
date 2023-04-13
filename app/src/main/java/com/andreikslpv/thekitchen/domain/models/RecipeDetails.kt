package com.andreikslpv.thekitchen.domain.models

data class RecipeDetails(
    val id: String = "re00000",
    val description: String = "",
    val steps: ArrayList<Step> = arrayListOf(),
    val ingredients: ArrayList<Ingredient> = arrayListOf(),
    val source: String = "",
    val imageDetails: String = "",
)