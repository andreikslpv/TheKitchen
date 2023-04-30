package com.andreikslpv.thekitchen.domain.models

data class FiltersSeparated(
    val query: String = "",
    var timeLimit: Int = 500,
    var categoriesDish: ArrayList<String> = arrayListOf(),
    var categoriesExclude: ArrayList<String> = arrayListOf(),
)
