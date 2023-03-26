package com.andreikslpv.thekitchen.domain.models

data class Category(
    val id: String = "",
    val name: String = "",
    val type: CategoryType = CategoryType(id = "ct00000", name = "null"),
)
