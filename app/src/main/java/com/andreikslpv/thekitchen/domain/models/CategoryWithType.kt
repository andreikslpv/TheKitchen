package com.andreikslpv.thekitchen.domain.models

data class CategoryWithType(
    val id: String = "ca00000",
    val name: String = "category_type",
    val type: CategoryType = CategoryType(),
    val image: String = "",
)
