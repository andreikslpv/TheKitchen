package com.andreikslpv.thekitchen.domain.models

data class User(
    var uid: String = "",
//    var displayName: String? = "",
//    var email: String? = "",
//    var photoUrl: String? = "",
//    var createdAt: Long = 0L,
    val favorites: ArrayList<String> = arrayListOf(),
    val history: ArrayList<String> = arrayListOf(),
    val defaultExclude: ArrayList<String> = arrayListOf(),
)
