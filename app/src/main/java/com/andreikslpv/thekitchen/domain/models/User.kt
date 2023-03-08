package com.andreikslpv.thekitchen.domain.models

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val createdAt: Long = 0L,
    )
