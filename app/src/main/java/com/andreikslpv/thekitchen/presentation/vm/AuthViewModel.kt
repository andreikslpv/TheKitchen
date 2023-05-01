package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.User
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var userRepository: UserRepository

    init {
        App.instance.dagger.inject(this)
    }

    fun signInWithGoogle(idToken: String) = liveData(Dispatchers.IO) {
        authRepository.firebaseSignInWithGoogle(idToken).collect { response ->
            emit(response)
        }
    }

    fun createUser() = liveData(Dispatchers.IO) {
        val user = User()
        authRepository.getCurrentUser()?.apply {
            user.uid = this.uid
            user.displayName = this.displayName
            user.email = this.email
            user.photoUrl = this.photoUrl.toString()
            user.createdAt = System.currentTimeMillis()
        }
        userRepository.createUser(user).collect { response ->
            emit(response)
        }
    }
}