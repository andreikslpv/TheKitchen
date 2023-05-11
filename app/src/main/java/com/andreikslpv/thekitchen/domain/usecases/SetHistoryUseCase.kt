package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository

class SetHistoryUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {
    private val historyMaxCount = 10

    fun execute(recipeId: String) {
        val user = authRepository.getCurrentUser()
        if (user != null) {
            // получаем текущую историю
            var currentHistory = userRepository.getHistory().value
            // удаляем из истории переданный рецепт, чтобы не было повторов в истории
            currentHistory = currentHistory.filter { it != recipeId }
            // добавляем в начало списка переданный рецепт
            var newHistory = listOf(recipeId)
            // доюавляем к списку sublist из истории без 0 элемента
            if (currentHistory.isNotEmpty())
                newHistory = newHistory.plus(currentHistory.subList(0, currentHistory.count()))
            // если длина списка больше максимума, то берем только первые до максимума
            if (newHistory.count() > historyMaxCount)
                newHistory = newHistory.take(historyMaxCount)
            userRepository.setHistory(user.uid, newHistory)
        }
    }
}