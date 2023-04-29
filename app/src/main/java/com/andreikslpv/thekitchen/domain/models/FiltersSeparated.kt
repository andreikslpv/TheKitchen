package com.andreikslpv.thekitchen.domain.models

data class FiltersSeparated(
    val query: String = "",
    var categoriesTime: Int = 500,
    var categoriesDish: ArrayList<String> = arrayListOf(),
    val categoriesExclude: ArrayList<String> = arrayListOf()
)
