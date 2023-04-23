package com.andreikslpv.thekitchen.domain.models

data class Filters(
    val query: String = "",
    private val categories: ArrayList<String> = arrayListOf(),
) {
    fun addCategory(category: String) {
        if (categories.contains(category)) return
        else categories.add(category)
        println("I/o adding: $categories")
    }

    fun removeCategory(category: String) {
        if (!categories.contains(category)) return
        else categories.remove(category)
        println("I/o removing: $categories")
    }

    fun getCategories() = categories
}

