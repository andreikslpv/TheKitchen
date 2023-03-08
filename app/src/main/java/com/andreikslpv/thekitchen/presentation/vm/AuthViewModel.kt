package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthViewModel : ViewModel() {

    @Inject
    lateinit var repository: AuthRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun signInWithGoogle(idToken: String) = liveData(Dispatchers.IO) {
        repository.firebaseSignInWithGoogle(idToken).collect { response ->
            emit(response)
        }
    }

    fun createUser() = liveData(Dispatchers.IO) {
        repository.createUserInFirestore().collect { response ->
            emit(response)
        }
    }
}