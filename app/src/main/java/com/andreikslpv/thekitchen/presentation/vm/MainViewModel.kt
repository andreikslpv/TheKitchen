package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var repository: AuthRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun signOut() = liveData(Dispatchers.IO) {
        repository.signOut().collect { response ->
            emit(response)
        }
    }

    @ExperimentalCoroutinesApi
    fun getAuthState() = liveData(Dispatchers.IO) {
        repository.getFirebaseAuthState().collect { response ->
            emit(response)
        }
    }

    fun getCurrentUser() = repository.getCurrentUser()
}