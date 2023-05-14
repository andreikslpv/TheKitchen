package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.Filters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetDefaultExcludeFromDbUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val categoryRepository: CategoryRepository,
) {

    fun execute(): Boolean {
        val user = authRepository.getCurrentUser()
        return if (user != null) {
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.getDefaultExclude().collect { exclude ->
                    if (exclude.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val newFilters = Filters()
                            newFilters.addCategories(exclude as ArrayList<String>)
                            categoryRepository.setFilters(newFilters)
                        }
                    }
                }
            }

            true
        } else {
            false
        }
    }
}