package com.andreikslpv.thekitchen.domain.models

import java.util.Locale

data class Filters(
    private val categories: ArrayList<String> = arrayListOf(),
    private var query: String = "",
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

    fun getCategoriesList() = categories

    fun setQuery(newQuery: String): Boolean {
        val temp = newQuery.lowercase(Locale.getDefault()).trim()
        return if(temp != query) {
            query = temp
            true
        } else false
    }

    fun getQuery() = query
}

