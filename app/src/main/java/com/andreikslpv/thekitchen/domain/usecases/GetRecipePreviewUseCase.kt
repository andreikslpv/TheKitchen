package com.andreikslpv.thekitchen.domain.usecases

import androidx.paging.PagingData
import com.andreikslpv.thekitchen.domain.CategoryRepository
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Filters
import com.andreikslpv.thekitchen.domain.models.FiltersSeparated
import com.andreikslpv.thekitchen.domain.models.RecipePreview
import kotlinx.coroutines.flow.Flow

class GetRecipePreviewUseCase(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
) {
    suspend fun execute(filters: Filters): Flow<PagingData<RecipePreview>> {
        val filtersSeparated = FiltersSeparated()
        // Проходимся по всему списку выбранных рецептов
        filters.getCategoriesList().forEach { filter ->
            categoryRepository.getAllCategories().value.forEach { category ->
                if (category.id == filter) {

                    if (category.type == CategoryType.DISH.value)
                        filtersSeparated.categoriesDish.add(filter)

                    if (category.type == CategoryType.TIME.value) {
                        filtersSeparated.timeLimit = getTimeFromCategoryName(category.name)
                    }

                    if (category.type == CategoryType.EXCLUDE.value) {
                        filtersSeparated.categoriesExclude.add(filter)
                    }
                }
            }
        }

        //Если категории типа блюд не заданы, то для запроса в БД считаем что выбраны все типы блюд
        if (filtersSeparated.categoriesDish.isEmpty())
            filtersSeparated.categoriesDish = categoryRepository.getAllCategories().value
                .filter { it.type == CategoryType.DISH.value }
                .map { it.id } as ArrayList<String>

        return recipeRepository.getRecipePreview(filtersSeparated)
    }

    private fun getTimeFromCategoryName(name: String): Int {
        val result = name.getOnlyDigital()
        return if (result > 0) result else FiltersSeparated().timeLimit
    }

    private fun String.getOnlyDigital(): Int {
        val temp = this.filter {
            it in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        }
        return try {
            temp.toInt()
        } catch (e: Exception) {
            0
        }
    }
}