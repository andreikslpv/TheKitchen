package com.andreikslpv.thekitchen.domain.models

data class User(
    var uid: String = "",
    val favorites: ArrayList<String> = arrayListOf(),
    val history: ArrayList<String> = arrayListOf(),
    val defaultExclude: ArrayList<String> = arrayListOf(),
    val shoppingList: ArrayList<ShoppingItem> = arrayListOf(),
)
