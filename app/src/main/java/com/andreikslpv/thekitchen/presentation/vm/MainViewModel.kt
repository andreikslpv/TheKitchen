package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var repository: AuthRepository

    val isUserAuthenticated get() = repository.isUserAuthenticatedInFirebase

    private val categoryFromHome = MutableLiveData("")

    init {
        App.instance.dagger.inject(this)
    }

    fun setCategoryFromHome(categoryId: String) {
        categoryFromHome.value = categoryId
    }

    fun getAndEraseCategoryFromHome(): String {
        val temp = categoryFromHome.value
        categoryFromHome.value = ""
        return temp ?: ""
    }

}