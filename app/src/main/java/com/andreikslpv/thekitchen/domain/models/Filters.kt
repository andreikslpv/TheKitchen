package com.andreikslpv.thekitchen.domain.models

data class Filters(
    val query: String = "",
    private val categories: ArrayList<String> = arrayListOf(),
    private val favorites: ArrayList<String> = arrayListOf(),
) {

    fun addCategories(categoryArray: Array<String>) {
        categories.clear()
        categories.addAll(categoryArray)
    }

    fun addCategory(category: String) {
        if (categories.contains(category)) return
        else categories.add(category)
    }

    fun removeCategory(category: String) {
        if (!categories.contains(category)) return
        else categories.remove(category)
    }

    fun getCategoriesList() = categories

    fun getCategoriesArray() = categories.toTypedArray()
}

