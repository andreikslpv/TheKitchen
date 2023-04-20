package com.andreikslpv.thekitchen.domain.models

data class CategoryWithType(
    val id: String = "ca00000",
    val name: String = "category_type",
    val type: CategoryTypeDB = CategoryTypeDB(),
    val image: String = "",
)
