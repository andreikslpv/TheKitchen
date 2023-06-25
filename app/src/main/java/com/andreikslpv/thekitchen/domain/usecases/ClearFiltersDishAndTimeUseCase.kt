package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.domain.CategoryRepository

class ClearFiltersDishAndTimeUseCase(
    private val categoryRepository: CategoryRepository,
) {

    fun execute() {
        categoryRepository.clearFiltersDishAndTime()
    }
}