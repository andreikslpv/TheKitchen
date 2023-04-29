package com.andreikslpv.thekitchen.domain.models

import com.google.firebase.firestore.Exclude

data class RecipePreview(
    val id: String = "re00000",
    val name: String = "recipe_preview",
    val categories: ArrayList<String> = arrayListOf(),
    val categoriesDish: ArrayList<String> = arrayListOf(),
    val ca00012: Boolean = false,
    val ca00013: Boolean = false,
    val ca00014: Boolean = false,
    val ca00015: Boolean = false,
    val ca00016: Boolean = false,
    val ca00017: Boolean = false,
    val time: Int = 0,
    val timeUnit: String = "un00001",
    val caloriesCount: Int = 0,
    val caloriesUnit: String = "un00002",
    val portions: Int = 0,
    val imagePreview: String = "",
    @Exclude
    val isFavorite: Boolean = false,
    @Exclude
    val isContainExclude: Boolean = false,
)