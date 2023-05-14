package com.andreikslpv.thekitchen.domain.models

data class Filters(
    private val categories: ArrayList<String> = arrayListOf(),
) {

    fun addCategories(categoryArray: ArrayList<String>) {
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

    fun removeCategories(removing: List<String>) {
        if (categories.isNotEmpty())
            categories.removeAll(removing.toSet())
    }

    fun changeCategoryStatus(category: String) {
        if (categories.contains(category)) categories.remove(category)
        else categories.add(category)
    }

    fun getCategoriesList() = categories

}

