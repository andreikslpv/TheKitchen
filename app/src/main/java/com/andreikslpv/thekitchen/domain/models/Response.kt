package com.andreikslpv.thekitchen.domain.models

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Failure(
        val errorMessage: String
    ): Response<Nothing>()
}