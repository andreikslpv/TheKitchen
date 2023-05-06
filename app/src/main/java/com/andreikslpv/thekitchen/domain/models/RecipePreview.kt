package com.andreikslpv.thekitchen.domain.models

import com.google.firebase.firestore.Exclude

data class RecipePreview(
    val id: String = "re00000",
    val name: String = "recipe_preview",
    val categoriesExclude: ArrayList<String> = arrayListOf(),
    val categoriesDish: ArrayList<String> = arrayListOf(),
    val time: Int = 0,
    val timeUnit: String = "un00001",
    val caloriesCount: Int = 0,
    val caloriesUnit: String = "un00002",
    val portions: Int = 0,
    val imagePreview: String = "",
    @Exclude
    var isFavorite: Boolean = false,
    @Exclude
    var isContainExclude: Boolean = false,
)